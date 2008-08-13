package org.nuxeo.ecm.core.search.api.client.search.results.impl;

import java.io.Serializable;
import java.util.HashMap;

import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.search.api.client.search.results.ResultItem;

public class CoreResultItem extends HashMap<String, Serializable> implements ResultItem{

    private static final long serialVersionUID = 4085969835020973753L;
    DocumentModel doc;

    public CoreResultItem(DocumentModel doc) {
        this.doc = doc;
    }

    public String getName() {
        if ( doc != null ){
            return doc.getName();
        }
        return "N/A";
    }

    public DocumentModel getDocumentModel() {
        return doc;
    }
    @Override
    public Serializable get(Object key) {
        if ( doc != null) {
            try { 
                return doc.getPropertyValue((String) key);
            } catch ( Exception e){ // do nothing}
            }
        }
        return null;
    }

}
