/*
 * (C) Copyright 2014 Nuxeo SA (http://nuxeo.com/) and contributors.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser General Public License
 * (LGPL) version 2.1 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl.html
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * Contributors:
 *     Nuxeo
 */

package org.nuxeo.elasticsearch.query;

import java.security.Principal;
import java.util.List;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.BaseQueryBuilder;
import org.elasticsearch.index.query.BoolFilterBuilder;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermsFilterBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.NuxeoPrincipal;
import org.nuxeo.ecm.core.api.SortInfo;
import org.nuxeo.ecm.core.security.SecurityService;
import org.nuxeo.elasticsearch.ElasticSearchComponent;

/**
 *
 * Base class for generating a SearchRequestBuilder
 *
 * @author <a href="mailto:tdelprat@nuxeo.com">Tiry</a>
 *
 */
public abstract class AbstractNuxeoESQueryBuilder {

    public TermsFilterBuilder getSecurityFilter(
            final Principal principal) {
        if (principal != null) {
            if (principal instanceof NuxeoPrincipal) {
                if (((NuxeoPrincipal)principal).isAdministrator()) {
                    return null;
                }
            }
            String[] principals = SecurityService
                    .getPrincipalsToCheck(principal);
            if (principals.length > 0) {
                return FilterBuilders.inFilter("ecm:acl", principals);
            }
        }
        return null;
    }

    /**
     * Append the sort option to the ES builder
     */
    public void addSortInfo(final SearchRequestBuilder builder,
            final SortInfo[] sortInfos) {
        for (SortInfo sortInfo : sortInfos) {
            builder.addSort(sortInfo.getSortColumn(), sortInfo
                    .getSortAscending() ? SortOrder.ASC : SortOrder.DESC);
        }

    }

    public SearchRequestBuilder makeSearchQueryBuilder(Client client,Principal principal, List<SortInfo> sortInfos) throws ClientException{
        return makeSearchQueryBuilder(client, principal, sortInfos, null, null);
    }

    public SearchRequestBuilder makeSearchQueryBuilder(Client client,Principal principal, List<SortInfo> sortInfos,Long pageSize, Long offset) throws ClientException{

        SearchRequestBuilder builder = client
                .prepareSearch(ElasticSearchComponent.MAIN_IDX)
                .setTypes(ElasticSearchComponent.NX_DOCUMENT)
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setFetchSource(ElasticSearchComponent.ID_FIELD, null);

        if (offset!=null) {
            builder.setFrom(offset.intValue());
        }
        if (pageSize!=null) {
            builder.setSize(pageSize.intValue());
        }

        SortInfo[] sortArray = null;
        if (sortInfos != null) {
            sortArray = sortInfos.toArray(new SortInfo[] {});
        }

        BoolFilterBuilder filter = FilterBuilders.boolFilter();

        TermsFilterBuilder securityFilter = getSecurityFilter(principal);
        if (securityFilter != null) {
            filter.must(securityFilter);
        }

        BaseQueryBuilder query = makeQueryBuilder(filter);

        builder.setQuery(QueryBuilders.filteredQuery(query, filter));

        // XXX should be able to detect empty filter
        //builder.setQuery(query);

        if (sortArray!=null) {
            addSortInfo(builder, sortArray);
        }

        // TODO: Add primarytype filter

        return builder;
    }

    protected abstract BaseQueryBuilder makeQueryBuilder(BoolFilterBuilder filter) throws ClientException;

}
