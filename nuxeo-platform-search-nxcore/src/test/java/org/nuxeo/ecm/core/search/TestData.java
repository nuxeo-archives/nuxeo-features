package org.nuxeo.ecm.core.search;

import java.util.Arrays;
import java.util.Calendar;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.PathRef;

public class TestData {



    public static final String SINGLE_BOOK_TITLE = "SingleBook";
    public static final String FRENCH_TITLE = "La Guerre et la paix";


    public static DocumentModel getDir( String name, CoreSession session) throws ClientException {
        try {
            return session.getDocument(new PathRef("/"+name));
        } catch (Exception e) {
            DocumentModel dir = session.createDocumentModel("/", name, "Folder");
            dir = session.createDocument(dir);
            session.save();
            return dir;
        }
    }

    public static DocumentModel createFakeBook(CoreSession session) throws ClientException{
        DocumentModel ret = session.createDocumentModel("/", SINGLE_BOOK_TITLE, "Book");
        ret.setPropertyValue("book:title", SINGLE_BOOK_TITLE);
        ret.setPropertyValue("book:barcode", "0018");

        ret = session.createDocument(ret);
        session.save();
        return ret;
    }


    public static DocumentModel createBook(CoreSession session) throws ClientException{
        DocumentModel dir = getDir("russian", session);

        DocumentModel ret = session.createDocumentModel(dir.getPathAsString(), "warpeace", "Book");
        ret.setPropertyValue("book:title", FRENCH_TITLE);
        ret.setPropertyValue("book:barcode", "0001");
        ret.setPropertyValue("dc:title", "War and Peace");
        ret.setPropertyValue("book:pages", 789);
        ret.setPropertyValue("book:content", "War & Peace full text");
        ret = session.createDocument(ret);
        session.save();
        return ret;
    }

    public static DocumentModel create2ndBook(CoreSession session) throws ClientException{
        Calendar calValue = Calendar.getInstance();
        calValue.clear();
        calValue.set(2003, 6, 15, 2, 22);
        
        
        DocumentModel dir = getDir("some", session);
        DocumentModel ret = session.createDocumentModel(dir.getPathAsString(), "sampleBook2", "Book");
        ret.setPropertyValue("book:title", FRENCH_TITLE);
        ret.setPropertyValue("book:barcode", "0002");
        ret.setPropertyValue("book:category", "autobio");
        ret.setPropertyValue("dc:title", "About Life");
        ret.setPropertyValue("book:pages", 437);
        ret.setPropertyValue("book:tags", new String[]{"philosophy", "people"});
        ret.setPropertyValue("book:content", "About life full text");
        ret = session.createDocument(ret);
        session.save();
        return ret;
    }


    public static void createBooks(CoreSession session, int n) throws ClientException{
        DocumentModel dir = getDir("some", session);
        for ( int i = 0 ; i < n ; i++) {

            Calendar calValue = Calendar.getInstance();
            calValue.clear();
            calValue.set(2007, 3, i + 1, 3, 57);

            DocumentModel doc = session.createDocumentModel(dir.getPathAsString(), "rev"+i, "Book");
            doc.setPropertyValue("book:title", "Révélations");
            doc.setPropertyValue("book:barcode", String.format("135%04d", i));
            doc.setPropertyValue("dc:title", "Revelations");
            doc.setPropertyValue("book:pages", 1000-i);
            doc.setPropertyValue("book:category", "novel");
            doc.setPropertyValue("book:tags", new String[]{"gossip", "people"});
            doc.setPropertyValue("book:content", "Revelation full text");
            doc.setPropertyValue("book:published", calValue);
            doc = session.createDocument(doc);
            session.save();
        }
    }



}
