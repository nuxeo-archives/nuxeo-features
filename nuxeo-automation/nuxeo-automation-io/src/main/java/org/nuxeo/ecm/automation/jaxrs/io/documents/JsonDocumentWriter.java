/*
 * Copyright (c) 2006-2011 Nuxeo SA (http://nuxeo.com/) and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     bstefanescu
 */
package org.nuxeo.ecm.automation.jaxrs.io.documents;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;
import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;
import org.nuxeo.common.utils.StringUtils;
import org.nuxeo.ecm.automation.io.services.contributor.HeaderDocEvaluationContext;
import org.nuxeo.ecm.automation.io.services.contributor.RestContributorService;
import org.nuxeo.ecm.automation.io.services.contributor.RestEvaluationContext;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.Lock;
import org.nuxeo.ecm.core.api.model.DocumentPart;
import org.nuxeo.ecm.core.api.model.Property;
import org.nuxeo.ecm.core.api.model.PropertyException;
import org.nuxeo.ecm.core.api.model.PropertyNotFoundException;
import org.nuxeo.ecm.core.api.model.impl.ArrayProperty;
import org.nuxeo.ecm.core.api.model.impl.ComplexProperty;
import org.nuxeo.ecm.core.api.model.impl.ListProperty;
import org.nuxeo.ecm.core.api.model.impl.primitives.BlobProperty;
import org.nuxeo.ecm.core.schema.types.ListType;
import org.nuxeo.ecm.core.schema.utils.DateParser;
import org.nuxeo.runtime.api.Framework;

/**
 * @author <a href="mailto:bs@nuxeo.com">Bogdan Stefanescu</a>
 */
@Provider
@Produces({ "application/json+nxentity", "application/json" })
public class JsonDocumentWriter implements MessageBodyWriter<DocumentModel> {

    public static final String DOCUMENT_PROPERTIES_HEADER = "X-NXDocumentProperties";

    private static final Log log = LogFactory.getLog(JsonDocumentWriter.class);

    @Context
    JsonFactory factory;

    @Context
    protected HttpHeaders headers;

    @Override
    public long getSize(DocumentModel arg0, Class<?> arg1, Type arg2,
            Annotation[] arg3, MediaType arg4) {
        return -1L;
    }

    @Override
    public boolean isWriteable(Class<?> arg0, Type arg1, Annotation[] arg2,
            MediaType arg3) {
        return DocumentModel.class.isAssignableFrom(arg0);
    }

    @Override
    public void writeTo(DocumentModel doc, Class<?> arg1, Type arg2,
            Annotation[] arg3, MediaType arg4,
            MultivaluedMap<String, Object> arg5, OutputStream out)
            throws IOException, WebApplicationException {
        try {
            // schema names: dublincore, file, ... or *
            List<String> props = headers.getRequestHeader(DOCUMENT_PROPERTIES_HEADER);
            String[] schemas = null;
            if (props != null && !props.isEmpty()) {
                schemas = StringUtils.split(props.get(0), ',', true);
            }
            writeDocument(out, doc, schemas);
        } catch (IOException e) {
            log.error("Failed to serialize document", e);
            throw e;
        } catch (Exception e) {
            log.error("Failed to serialize document", e);
            throw new WebApplicationException(e, 500);
        }
    }

    public void writeDocument(OutputStream out, DocumentModel doc,
            String[] schemas) throws Exception {
        writeDocument(out, doc, schemas, null);
    }

    /**
     * @since 5.6
     */
    public void writeDocument(OutputStream out, DocumentModel doc,
            String[] schemas, Map<String, String> contextParameters)
            throws Exception {
        writeDocument(factory.createJsonGenerator(out, JsonEncoding.UTF8), doc,
                schemas, contextParameters, headers);
    }

    public static void writeDocument(JsonGenerator jg, DocumentModel doc,
            String[] schemas) throws Exception {
        writeDocument(jg, doc, schemas, null);
    }

    /**
     * @since 5.6
     */
    public static void writeDocument(JsonGenerator jg, DocumentModel doc,
            String[] schemas, Map<String, String> contextParameters)
            throws Exception {
        writeDocument(jg, doc, schemas, contextParameters, null);
    }

    /**
     * @since 5.7.3
     */
    public static void writeDocument(JsonGenerator jg, DocumentModel doc,
            String[] schemas, Map<String, String> contextParameters,
            HttpHeaders headers) throws Exception {
        jg.writeStartObject();
        jg.writeStringField("entity-type", "document");
        jg.writeStringField("repository", doc.getRepositoryName());
        jg.writeStringField("uid", doc.getId());
        jg.writeStringField("path", doc.getPathAsString());
        jg.writeStringField("type", doc.getType());
        jg.writeStringField("state", doc.getCurrentLifeCycleState());
        jg.writeStringField("versionLabel", doc.getVersionLabel());
        jg.writeBooleanField("isCheckedOut", doc.isCheckedOut());
        Lock lock = doc.getLockInfo();
        if (lock != null) {
            jg.writeStringField("lockOwner", lock.getOwner());
            jg.writeStringField(
                    "lockCreated",
                    ISODateTimeFormat.dateTime().print(
                            new DateTime(lock.getCreated())));
        }
        jg.writeStringField("title", doc.getTitle());
        try {
            Calendar cal = (Calendar) doc.getPropertyValue("dc:modified");
            if (cal != null) {
                jg.writeStringField("lastModified",
                        DateParser.formatW3CDateTime(cal.getTime()));
            }
        } catch (PropertyNotFoundException e) {
            // ignore
        }

        if (schemas != null && schemas.length > 0) {
            jg.writeObjectFieldStart("properties");
            if (schemas.length == 1 && "*".equals(schemas[0])) {
                // full document
                for (String schema : doc.getSchemas()) {
                    writeProperties(jg, doc, schema);
                }
            } else {
                for (String schema : schemas) {
                    writeProperties(jg, doc, schema);
                }
            }
            jg.writeEndObject();
        }

        jg.writeArrayFieldStart("facets");
        for (String facet : doc.getFacets()) {
            jg.writeString(facet);
        }
        jg.writeEndArray();
        jg.writeStringField("changeToken", doc.getChangeToken());

        jg.writeObjectFieldStart("contextParameters");
        if (contextParameters != null && !contextParameters.isEmpty()) {
            for (Map.Entry<String, String> parameter : contextParameters.entrySet()) {
                jg.writeStringField(parameter.getKey(), parameter.getValue());
            }
        }

        writeRestContributions(jg, doc, headers);
        jg.writeEndObject();

        jg.writeEndObject();
        jg.flush();
    }

    /**
     * @param jg
     * @param doc
     * @param headers
     * @throws ClientException
     * @throws IOException
     * @throws JsonGenerationException
     *
     * @since 5.7.3
     */
    protected static void writeRestContributions(JsonGenerator jg,
            DocumentModel doc, HttpHeaders headers)
            throws JsonGenerationException, IOException, ClientException {
        RestContributorService rcs = Framework.getLocalService(RestContributorService.class);
        RestEvaluationContext ec = new HeaderDocEvaluationContext(doc, headers);
        rcs.writeContext(jg, ec);
    }

    protected static void writeProperties(JsonGenerator jg, DocumentModel doc,
            String schema) throws Exception {
        DocumentPart part = doc.getPart(schema);
        if (part == null) {
            return;
        }
        String prefix = part.getSchema().getNamespace().prefix;
        if (prefix == null || prefix.length() == 0) {
            prefix = schema;
        }
        prefix = prefix + ":";
        String filesBaseUrl = "files/" + doc.getId() + "?path=";
        for (Property p : part.getChildren()) {
            jg.writeFieldName(prefix + p.getField().getName().getLocalName());
            writePropertyValue(jg, p, filesBaseUrl);
        }
    }

    /**
     * Converts the value of the given core property to JSON format. The given
     * filesBaseUrl is the baseUrl that can be used to locate blob content and
     * is useful to generate blob urls.
     */
    public static void writePropertyValue(JsonGenerator jg, Property prop,
            String filesBaseUrl) throws PropertyException,
            JsonGenerationException, IOException {
        if (prop.isScalar()) {
            writeScalarPropertyValue(jg, prop);
        } else if (prop.isList()) {
            writeListPropertyValue(jg, prop, filesBaseUrl);
        } else {
            if (prop.isPhantom()) {
                jg.writeNull();
            } else if (prop instanceof BlobProperty) { // a blob
                writeBlobPropertyValue(jg, prop, filesBaseUrl);
            } else { // a complex property
                writeMapPropertyValue(jg, (ComplexProperty) prop, filesBaseUrl);
            }
        }
    }

    protected static void writeScalarPropertyValue(JsonGenerator jg,
            Property prop) throws PropertyException, JsonGenerationException,
            IOException {
        org.nuxeo.ecm.core.schema.types.Type type = prop.getType();
        Object v = prop.getValue();
        if (v == null) {
            jg.writeNull();
        } else {
            jg.writeString(type.encode(v));
        }
    }

    protected static void writeListPropertyValue(JsonGenerator jg,
            Property prop, String filesBaseUrl) throws PropertyException,
            JsonGenerationException, IOException {
        jg.writeStartArray();
        if (prop instanceof ArrayProperty) {
            Object[] ar = (Object[]) prop.getValue();
            if (ar == null) {
                return;
            }
            org.nuxeo.ecm.core.schema.types.Type type = ((ListType) prop.getType()).getFieldType();
            for (Object o : ar) {
                jg.writeString(type.encode(o));
            }
        } else {
            ListProperty listp = (ListProperty) prop;
            for (Property p : listp.getChildren()) {
                writePropertyValue(jg, p, filesBaseUrl);
            }
        }
        jg.writeEndArray();
    }

    protected static void writeMapPropertyValue(JsonGenerator jg,
            ComplexProperty prop, String filesBaseUrl)
            throws JsonGenerationException, IOException, PropertyException  {
        jg.writeStartObject();
        for (Property p : prop.getChildren()) {
            jg.writeFieldName(p.getName());
            writePropertyValue(jg, p, filesBaseUrl);
        }
        jg.writeEndObject();
    }

    protected static void writeBlobPropertyValue(JsonGenerator jg,
            Property prop, String filesBaseUrl) throws PropertyException,
            JsonGenerationException, IOException {
        Blob blob = (Blob) prop.getValue();
        if (blob == null) {
            jg.writeNull();
            return;
        }
        jg.writeStartObject();
        String v = blob.getFilename();
        if (v == null) {
            jg.writeNullField("name");
        } else {
            jg.writeStringField("name", v);
        }
        v = blob.getMimeType();
        if (v == null) {
            jg.writeNullField("mime-type");
        } else {
            jg.writeStringField("mime-type", v);
        }
        v = blob.getEncoding();
        if (v == null) {
            jg.writeNullField("encoding");
        } else {
            jg.writeStringField("encoding", v);
        }
        v = blob.getDigest();
        if (v == null) {
            jg.writeNullField("digest");
        } else {
            jg.writeStringField("digest", v);
        }
        jg.writeStringField("length", Long.toString(blob.getLength()));
        jg.writeStringField("data",
                filesBaseUrl + URLEncoder.encode(prop.getPath(), "UTF-8"));
        jg.writeEndObject();
    }

}
