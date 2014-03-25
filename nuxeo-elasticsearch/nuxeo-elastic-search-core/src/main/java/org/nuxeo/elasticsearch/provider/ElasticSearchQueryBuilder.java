package org.nuxeo.elasticsearch.provider;

import java.util.Calendar;
import java.util.Collection;

import org.elasticsearch.index.query.BaseQueryBuilder;
import org.elasticsearch.index.query.BoolFilterBuilder;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.index.query.RangeFilterBuilder;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.ClientRuntimeException;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.schema.utils.DateParser;
import org.nuxeo.ecm.platform.query.api.PageProviderDefinition;
import org.nuxeo.ecm.platform.query.api.PredicateDefinition;
import org.nuxeo.ecm.platform.query.api.PredicateFieldDefinition;
import org.nuxeo.ecm.platform.query.api.WhereClauseDefinition;
import org.nuxeo.elasticsearch.query.AbstractNuxeoESQueryBuilder;

public class ElasticSearchQueryBuilder extends AbstractNuxeoESQueryBuilder {

    protected WhereClauseDefinition whereClause;

    protected String pattern;

    protected DocumentModel model;

    protected Object[] parameters;

    public ElasticSearchQueryBuilder(PageProviderDefinition def,
            DocumentModel model, Object[] parameters) {
        this(def.getWhereClause(), def.getPattern(), model, parameters);
    }

    public ElasticSearchQueryBuilder(WhereClauseDefinition whereClause,
            String pattern, DocumentModel model, Object[] parameters) {
        this.whereClause = whereClause;
        this.pattern = pattern;
        this.model = model;
        this.parameters = parameters;
    }

    @Override
    protected BaseQueryBuilder makeQueryBuilder(BoolFilterBuilder filter)
            throws ClientException {

        if (whereClause == null) {
            return makeQuery(pattern, parameters);
        } else {
            if (model == null) {
                throw new ClientException(
                        "Cannot build query : no associated DocumentModel");
            }
            return makeQuery(model, whereClause, parameters, filter);
        }
    }

    /**
     * Create a ES request from a PP pattern
     */
    public QueryStringQueryBuilder makeQuery(final String pattern,
            final Object[] params) throws ClientException {
        String query = pattern;
        for (int i = 0; i < params.length; i++) {
            query = query.replaceFirst("\\?", convertParam(params[i]));
        }
        return QueryBuilders.queryString(query);
    }

    /**
     * Create a ES request from a PP whereClause
     */
    public BoolQueryBuilder makeQuery(final DocumentModel model,
            final WhereClauseDefinition whereClause, final Object[] params,
            BoolFilterBuilder filter) throws ClientException {

        assert (model != null);
        assert (whereClause != null);

        BoolQueryBuilder query = QueryBuilders.boolQuery();

        // Fixed part handled as query_string
        String fixedPart = whereClause.getFixedPart();
        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                fixedPart = fixedPart.replaceFirst("\\?",
                        convertParam(params[i]));
            }
            query.must(QueryBuilders.queryString(fixedPart));
        }
        // Process predicates
        for (PredicateDefinition predicate : whereClause.getPredicates()) {
            PredicateFieldDefinition[] fieldDef = predicate.getValues();
            Object[] val;
            try {
                val = new Object[fieldDef.length];
                for (int fidx = 0; fidx < fieldDef.length; fidx++) {
                    if (fieldDef[fidx].getXpath() != null) {
                        val[fidx] = model.getPropertyValue(fieldDef[fidx].getXpath());
                    } else {
                        val[fidx] = model.getProperty(
                                fieldDef[fidx].getSchema(),
                                fieldDef[fidx].getName());
                    }
                }
            } catch (Exception e) {
                throw new ClientRuntimeException(e);
            }
            if (!isNonNullParam(val)) {
                // skip predicate where all values are null
                continue;
            }
            String field = predicate.getParameter();
            String operator = predicate.getOperator().toUpperCase();
            Object firstValue = val[0];
            if (operator.equals("=")) {
                filter.must(FilterBuilders.termFilter(field, firstValue));
            } else if (operator.equals("!=") || operator.equals("<>")) {
                filter.mustNot(FilterBuilders.termFilter(field, firstValue));
            } else if (operator.equals("<")) {
                filter.must(FilterBuilders.rangeFilter(field).lt(firstValue));
            } else if (operator.equals(">")) {
                filter.must(FilterBuilders.rangeFilter(field).gt(firstValue));
            } else if (operator.equals("<=")) {
                filter.must(FilterBuilders.rangeFilter(field).lte(firstValue));
            } else if (operator.equals(">=")) {
                filter.must(FilterBuilders.rangeFilter(field).gte(firstValue));
            } else if (operator.equals("LIKE")) {
                // TODO convert like pattern
                query.must(QueryBuilders.regexpQuery(field, (String) firstValue));
            } else if (operator.equals("ILIKE")) {
                // TODO convert ilike pattern
                query.must(QueryBuilders.regexpQuery(field, (String) firstValue));
            } else if (operator.equals("IN")) {
                if (val[0] instanceof Collection<?>) {
                    // TODO check if all iterable are collection
                    Collection<?> vals = (Collection<?>) val[0];
                    Object[] valArray = vals.toArray(new Object[vals.size()]);
                    filter.must(FilterBuilders.inFilter(field, valArray));
                } else if (val[0] instanceof Object[]) {
                    Object[] vals = (Object[]) val[0];
                    filter.must(FilterBuilders.inFilter(field, vals));
                }
            } else if (operator.equals("BETWEEN")) {
                Object startValue = firstValue;
                Object endValue = null;
                if (val.length > 1) {
                    endValue = val[1];
                }
                RangeFilterBuilder range = FilterBuilders.rangeFilter(field);
                if (startValue != null) {
                    range.from(startValue);
                }
                if (endValue != null) {
                    range.to(endValue);
                }
                filter.must(range);
            } else if (operator.equals("IS NOT NULL")) {
                filter.must(FilterBuilders.existsFilter(field));
            } else if (operator.equals("IS NULL")) {
                filter.mustNot(FilterBuilders.existsFilter(field));
            } else if (operator.equals("FULLTEXT")) {
                // convention on the name of the fulltext analyzer to use
                query.must(QueryBuilders.simpleQueryString((String) firstValue).field(
                        "_all").analyzer("fulltext"));
            } else if (operator.equals("STARTSWITH")) {
                if (field.equals("ecm:path")) {
                    query.must(QueryBuilders.matchQuery(field + ".children",
                            firstValue));
                } else {
                    query.must(QueryBuilders.prefixQuery(field,
                            (String) firstValue));
                }
            } else {
                throw new ClientException("Not implemented operator: "
                        + operator);
            }
        }
        return query;
    }

    protected String convertFieldName(final String parameter) {
        return parameter.replace(":", "\\:");
    }

    /**
     * Convert a param for a query_string style
     */
    protected String convertParam(final Object param) {
        String ret;
        if (param == null) {
            ret = "";
        } else if (param instanceof Boolean) {
            ret = ((Boolean) param).toString();
        } else if (param instanceof Calendar) {
            ret = DateParser.formatW3CDateTime(((Calendar) param).getTime());
        } else if (param instanceof Double) {
            ret = ((Double) param).toString();
        } else if (param instanceof Integer) {
            ret = ((Integer) param).toString();
        } else {
            ret = "\"" + param.toString() + "\"";
        }
        return ret;
    }

    protected boolean isNonNullParam(final Object[] val) {
        if (val == null) {
            return false;
        }
        for (Object v : val) {
            if (v != null) {
                if (v instanceof String) {
                    if (!((String) v).isEmpty()) {
                        return true;
                    }
                } else if (v instanceof String[]) {
                    if (((String[]) v).length > 0) {
                        return true;
                    }
                } else {
                    return true;
                }
            }
        }
        return false;
    }

}
