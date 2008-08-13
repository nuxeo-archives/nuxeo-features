package org.nuxeo.ecm.core.search;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.nuxeo.ecm.core.api.CoreInstance;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.api.PagedDocumentsProvider;
import org.nuxeo.ecm.core.api.security.SecurityConstants;
import org.nuxeo.ecm.core.query.sql.SQLQueryParser;
import org.nuxeo.ecm.core.query.sql.model.SQLQuery;
import org.nuxeo.ecm.core.search.api.backend.SearchEngineBackend;
import org.nuxeo.ecm.core.search.api.backend.indexing.resources.ResolvedResources;
import org.nuxeo.ecm.core.search.api.backend.indexing.resources.factory.BuiltinDocumentFields;
import org.nuxeo.ecm.core.search.api.client.SearchException;
import org.nuxeo.ecm.core.search.api.client.SearchService;
import org.nuxeo.ecm.core.search.api.client.query.ComposedNXQuery;
import org.nuxeo.ecm.core.search.api.client.query.QueryException;
import org.nuxeo.ecm.core.search.api.client.query.impl.ComposedNXQueryImpl;
import org.nuxeo.ecm.core.search.api.client.search.results.ResultItem;
import org.nuxeo.ecm.core.search.api.client.search.results.ResultSet;
import org.nuxeo.ecm.core.search.api.client.search.results.document.SearchPageProvider;
import org.nuxeo.runtime.api.Framework;
import org.nuxeo.runtime.test.NXRuntimeTestCase;

public class TestSearchServiceCoreImpl extends NXRuntimeTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        deployBundle("nuxeo-core-schema");
        deployBundle("nuxeo-core-api");
        deployBundle("nuxeo-core");
        deployBundle("nuxeo-core-jcr-connector");
        // deployBundle("nuxeo-core-jcr-connector-test");
        deployBundle("nuxeo-platform-search-api");
        // deployBundle("nuxeo-platform-search-facade");
        deployBundle("nuxeo-platform-search-nxcore");
        deployContrib("DemoRepository.xml");
        deployContrib("nxsearch-backendtest-types-contrib.xml");
        initSearchService();
        initCoreSession();
    }

    SearchService service;

    CoreSession session;

    protected void initSearchService() throws Exception {
        if (service == null) {
            service = Framework.getService(SearchService.class);
        }
    }

    protected void initCoreSession() throws Exception {
        if (session == null) {
            Map<String, Serializable> ctx = new HashMap<String, Serializable>();
            ctx.put("username", SecurityConstants.ADMINISTRATOR);
            session = CoreInstance.getInstance().open("default", ctx);
        }
    }

    public void testInitialization() {
        assertNotNull(service);
        assertNotNull(session);
    }

    public void testSimpleQuery() throws SearchException, QueryException {
        SQLQuery nxqlQuery = SQLQueryParser.parse("SELECT * FROM Document ");
        ResultSet results = service.searchQuery(new ComposedNXQueryImpl(
                nxqlQuery), 0, 100);
        System.out.println(results.size());
    }

    public void testOneDoc() throws Exception {
        DocumentModel doc = TestData.createFakeBook(session);

        SQLQuery nxqlQuery = SQLQueryParser.parse(String.format(
                "SELECT * FROM Book WHERE book:title='%s' ",
                TestData.SINGLE_BOOK_TITLE));
        ResultSet results = service.searchQuery(new ComposedNXQueryImpl(
                nxqlQuery), 0, 100);
        assertEquals(1, results.getTotalHits());
        assertEquals(1, results.getPageHits());
        assertEquals(0, results.getOffset());
        assertEquals(100, results.getRange());
        assertFalse(results.hasNextPage());
        assertTrue(results.isFirstPage());

        // check that we have all stored properties
        // ResultItem resItem = results.get(0);
        // assertEquals("About Life", resItem.get("dc:title"));
        // assertEquals("0000", resItem.get("bk:barcode"));
        // assertEquals("Abstracts aren't indexed but stored",
        // resItem.get("bk:abstract"));
        // assertEquals(new PathRef("some/path"),
        // resItem.get(BuiltinDocumentFields.FIELD_DOC_REF));
        // assertFalse(resItem.containsKey("bk:contents"));
        // assertEquals(437L, resItem.get("bk:pages"));

        assertNull(results.nextPage());

        // Now replay()
        ResultSet replayed = results.replay();
        // full assertEquals not directly possible
        assertEquals(results.getTotalHits(), replayed.getTotalHits());
        // assertEquals(results.get(0).get("bk:barcode"), replayed.get(0).get(
        // "bk:barcode"));

        // Delete the resource and use replay() to check
        // This actually relies on internals of the currently only
        // existing backend, that merges all schema resources into a single one
        // and use the join id as primary id for this merging
        // Disabled because doesn't make sense anymore now that the backend
        // is allowed to actually merge several atomic resources into one.
        // TODO must specify what should happen
        service.deleteAtomicResource("agg_id");
        replayed = results.replay();
        assertEquals(1, replayed.getTotalHits());
        assertEquals(1, replayed.getPageHits());
        assertEquals(0, replayed.getOffset());
        assertEquals(100, replayed.getRange());
        assertFalse(replayed.hasNextPage());
        assertTrue(replayed.isFirstPage());

        // Recreate
        replayed = results.replay();
        assertEquals(1, replayed.getTotalHits());

        replayed = results.replay();
        assertEquals(1, replayed.getTotalHits());
    }

    public void testRefQuery() throws Exception {
        DocumentModel doc = TestData.createBook(session);
        ResultSet results = service.searchQuery(composeQuery(String.format(
                "SELECT * FROM Document WHERE %s = '%s' ",
                BuiltinDocumentFields.FIELD_DOC_REF, doc.getId())), 0, 100);
        assertEquals(1, results.getTotalHits());
    }
    
    public void testIntBoolQuery() throws Exception {
        TestData.createBook(session);
        TestData.create2ndBook(session);
        ResultSet results;

         results = service.searchQuery( composeQuery("SELECT * FROM Document WHERE book:pages = 437"), 0, 100); 
         assertEquals(1,results.getTotalHits()); 
          assertEquals("About Life", results.get(0).get("dc:title"));
         
          results = service.searchQuery( composeQuery("SELECT * FROM Document WHERE book:pages > 437"), 0, 100); 
          assertEquals(1, results.getTotalHits());
          
          results = service.searchQuery( composeQuery("SELECT * FROM Document WHERE book:pages >= 437"), 0, 100);
          assertEquals(2, results.getTotalHits());
         
          results = service.searchQuery( composeQuery("SELECT * FROM Document WHERE book:pages < 789"), 0, 100); 
          assertEquals(1, results.getTotalHits()); 
          assertEquals("About Life", results.get(0).get("dc:title"));
         
          results = service.searchQuery( composeQuery("SELECT * FROM Document WHERE book:pages <= 789"), 0, 100);
          assertEquals(2, results.getTotalHits()); 
         
        // BOOLEAN
        results = service.searchQuery(composeQuery("SELECT * FROM Document "
                + "WHERE ecm:isCheckedInVersion = 1"), 0, 100);

        assertEquals(0, results.getTotalHits());

        results = service.searchQuery(composeQuery("SELECT * FROM Document "
                + "WHERE ecm:isCheckedInVersion = 0"), 0, 100);

        // 2 book & 2 directories 
        assertEquals(4, results.getTotalHits());
    }
    
    public void testBunch() throws Exception {
        TestData.create2ndBook(session);
        TestData.createBooks(session, 12);

        // Looking for About Life
        ResultSet results = service.searchQuery(
                composeQuery("SELECT * FROM Document WHERE book:barcode='0002'"),
                0, 100);
        assertEquals(1, results.getTotalHits());
        assertEquals(1, results.getPageHits());
        assertEquals(0, results.getOffset());
        assertEquals(100, results.getRange());
        assertFalse(results.hasNextPage());
        assertTrue(results.isFirstPage());
        ResultItem resItem = results.get(0);

        // check that this the correct one
        assertEquals("About Life", resItem.get("dc:title"));

//        // Looking for Revelations
        results = service.searchQuery(
                composeQuery("SELECT * FROM Document WHERE dc:title='Revelations'"),
                0, 10);
        assertEquals(12, results.getTotalHits());
        assertEquals(10, results.getPageHits());
        assertEquals(0, results.getOffset());
        assertEquals(10, results.getRange());
        assertTrue(results.hasNextPage());
        assertTrue(results.isFirstPage());

        // check that this is the correct one.
        resItem = results.get(0);
        assertEquals("Revelations", resItem.get("dc:title"));

        results = results.nextPage();
        assertEquals(12, results.getTotalHits());
        assertEquals(2, results.getPageHits());
        assertEquals(10, results.getOffset());
        assertEquals(10, results.getRange());
        assertFalse(results.hasNextPage());
        assertFalse(results.isFirstPage());

        // check document model formation through wrapping in page provider
        PagedDocumentsProvider provider = new SearchPageProvider(results);
        DocumentModelList docModels = provider.getCurrentPage();

        assertEquals("Revelations", docModels.get(0).getProperty("dublincore",
                "title"));

//        // Barcode is not full-text indexed
        results = service.searchQuery(
                composeQuery("SELECT * FROM Document WHERE book:barcode='RVL'"),
                0, 2);
        assertEquals(0, results.getTotalHits());

        // STARTSWITH on a non text field
//        results = service.searchQuery(
//                composeQuery("SELECT * FROM Document WHERE book:category "
//                        + "STARTSWITH 'auto' "), 0, 2);
//        assertEquals(1, results.getTotalHits());
//        assertEquals("About Life", results.get(0).get("dc:title"));
//
        //
        // IN queries
        //
        results = service.searchQuery(composeQuery("SELECT * FROM Document "
                + "WHERE book:category IN ('autobio', 'novel')"), 0, 3);
        assertEquals(12 + 1, results.getTotalHits());
        results = service.searchQuery(
                composeQuery("SELECT * FROM Document WHERE "
                        + "book:category IN ('autobio', 'junk')"), 0, 1);
        assertEquals(1, results.getTotalHits());

        // Tests with a multiple Keyword property
        results = service.searchQuery(
                composeQuery("SELECT * FROM Document WHERE book:tags = 'people'"),
                0, 1);
        assertEquals(13, results.getTotalHits());
        results = service.searchQuery(
                composeQuery("SELECT * FROM Document WHERE book:tags = 'philosophy'"),
                0, 1);
        assertEquals(1, results.getTotalHits());
        
        
//        // IN result in intersection
        results = service.searchQuery(composeQuery("SELECT * FROM Document "
                + "WHERE book:tags IN ('gossip', 'philosophy')"), 0, 1);
        assertEquals(13, results.getTotalHits());
        results = service.searchQuery(
                composeQuery("SELECT * FROM Document WHERE book:tags = 'gossip'"),
                0, 1);
        assertEquals(12, results.getTotalHits());

        // Boolean
        results = service.searchQuery(
                composeQuery("SELECT * FROM Document WHERE "
                        + "book:category IN ('novel', 'junk') "
                        + "AND dc:title = 'Life'"), 0, 1);
        assertEquals(0, results.getTotalHits());

        // Path Queries
        results = service.searchQuery(
                composeQuery("SELECT * FROM Document WHERE "
                        + BuiltinDocumentFields.FIELD_DOC_PATH
                        + " STARTSWITH 'some'"), 0, 100);
        assertEquals(12 + 1, results.getTotalHits());
//
//        // Add War and Peace for more path prefixes
        TestData.createBook(session);

        results = service.searchQuery(
                composeQuery("SELECT * FROM Document WHERE "
                        + BuiltinDocumentFields.FIELD_DOC_PATH
                        + " STARTSWITH 'russian'"), 0, 100);
        assertEquals(1, results.getTotalHits());
        assertEquals("War and Peace", results.get(0).get("dc:title"));


//        results = service.searchQuery(
//                composeQuery("SELECT * FROM Document WHERE "
//                        + BuiltinDocumentFields.FIELD_FULLTEXT
//                        + "= 'text full'"), 0, 100);
//        assertEquals(0, results.getTotalHits());
//
//        results = service.searchQuery(
//                composeQuery("SELECT * FROM Document WHERE "
//                        + BuiltinDocumentFields.FIELD_FULLTEXT
//                        + " LIKE '+About +life optional stuff'"), 0, 100);
//        assertEquals(1, results.getTotalHits());
//        assertEquals("About Life", results.get(0).get("dc:title"));
    }

    
    
    public void testTimeStampDateQueries() throws Exception {
        
        TestData.createBooks(session, 12);
        ResultSet results;

        // shift by 1 because for Calendar january is month #0
        results = service.searchQuery(
                composeQuery("SELECT * FROM Document WHERE "
                        + "book:published = TIMESTAMP '2007-04-12 03:57:00'"), 0,
                100);
        assertEquals(1, results.getTotalHits());
        assertEquals("1350011", results.get(0).get("book:barcode"));

        results = service.searchQuery(
                composeQuery("SELECT * FROM Document WHERE "
                        + "book:published > TIMESTAMP '2007-04-11 03:56:00'"), 0,
                100);

        assertEquals(2, results.getTotalHits());
        assertEquals("1350010", results.get(0).get("book:barcode"));
        assertEquals("1350011", results.get(1).get("book:barcode"));

        results = service.searchQuery(
                composeQuery("SELECT * FROM Document WHERE "
                        + "book:published >= TIMESTAMP '2007-04-11 03:57:00'"),
                0, 100);

        assertEquals(2, results.getTotalHits());
        assertEquals("1350010", results.get(0).get("book:barcode"));
        assertEquals("1350011", results.get(1).get("book:barcode"));

        results = service.searchQuery(
                composeQuery("SELECT * FROM Document WHERE "
                        + "book:published < TIMESTAMP '2007-04-05 03:57:00'"), 0,
                100);
        assertEquals(4, results.getTotalHits());
        assertEquals("1350000", results.get(0).get("book:barcode"));
        assertEquals("1350001", results.get(1).get("book:barcode"));
        assertEquals("1350002", results.get(2).get("book:barcode"));
        assertEquals("1350003", results.get(3).get("book:barcode"));

        results = service.searchQuery(
                composeQuery("SELECT * FROM Document WHERE "
                        + "book:published = DATE '2007-04-05'"), 0, 100);
        assertEquals(1, results.getTotalHits());
        assertEquals("1350004", results.get(0).get("book:barcode"));

        // TODO test boundaries with a doc created at 0:0 AM
        // (delicate with timezone info.)

//        results = service.searchQuery(
//                composeQuery("SELECT * FROM Document WHERE "
//                        + "book:published BETWEEN DATE '2007-04-05' AND "
//                        + "DATE '2007-04-07'"), 0, 100);
//        assertEquals(3, results.getTotalHits());
//        assertEquals("1350004", results.get(0).get("book:barcode"));
//        assertEquals("1350005", results.get(1).get("book:barcode"));
//        assertEquals("1350006", results.get(2).get("book:barcode"));
    }
    
    
    
    
    public void testFromClause() throws Exception {
        TestData.create2ndBook(session);
        
        assertEquals(1, service.searchQuery(composeQuery("SELECT * FROM Book"),
                0, 100).getTotalHits());
        // Book is declared to extend Folder
        
//        try {
//            @SuppressWarnings("unused")
//            ResultSet dummy = service.searchQuery(
//                    composeQuery("SELECT * FROM Unknown"), 0, 1);
//            fail("Expected a QueryException");
//        } catch (QueryException e) {
//        }

        // FROM and WHERE
        assertEquals(1, service.searchQuery(
                composeQuery("SELECT * FROM Folder WHERE book:barcode = '0002'"),
                0, 100).getTotalHits());
        assertEquals(0, service.searchQuery(
                composeQuery("SELECT * FROM Folder WHERE book:barcode = '0001'"),
                0, 100).getTotalHits());
    }
    
    
    public void testIntOrderClauses() throws Exception {
        TestData.createBook(session);
        TestData.create2ndBook(session);
        
        ResultSet results;
        results = service.searchQuery(
                composeQuery("SELECT * FROM Book ORDER BY book:pages"), 0,
                100);
        assertEquals(2, results.getTotalHits());
        assertEquals("About Life", results.get(0).get("dc:title"));
        assertEquals("War and Peace", results.get(1).get("dc:title"));

        results = service.searchQuery(
                composeQuery("SELECT * FROM Book ORDER BY book:pages DESC"),
                0, 100);
        assertEquals(2, results.getTotalHits());
        assertEquals("War and Peace", results.get(0).get("dc:title"));
        assertEquals("About Life", results.get(1).get("dc:title"));
    }
    
    
    public void testTextOrderClauses() throws Exception {
        TestData.createBook(session);
        TestData.create2ndBook(session);

        ResultSet results;
        results = service.searchQuery(
                composeQuery("SELECT * FROM Book ORDER BY dc:title"), 0,
                100);
        assertEquals(2, results.getTotalHits());
        assertEquals("About Life", results.get(0).get("dc:title"));
        assertEquals("War and Peace", results.get(1).get("dc:title"));

        results = service.searchQuery(
                composeQuery("SELECT * FROM Book ORDER BY dc:title DESC"),
                0, 100);
        assertEquals(2, results.getTotalHits());
        assertEquals("War and Peace", results.get(0).get("dc:title"));
        assertEquals("About Life", results.get(1).get("dc:title"));

        // Now a text field that's sortable in a case-insensitive way
//        results = service.searchQuery(
//                composeQuery("SELECT * FROM Book ORDER BY bk:frenchtitle"), 0,
//                100);
//        assertEquals(2, results.getTotalHits());
//        assertEquals("War and Peace", results.get(0).get("dc:title")); // La Guerre
//        assertEquals("About Life", results.get(1).get("dc:title")); // La mechante
//        results = service.searchQuery(
//                composeQuery("SELECT * FROM Book ORDER BY book:frenchtitle DESC"), 0,
//                100);
//        assertEquals("About Life", results.get(0).get("dc:title")); // La mechante
//        assertEquals("War and Peace", results.get(1).get("dc:title")); // La Guerre


        // Now a text field that's not declared as sortable. We can get a
        // QueryException and nothing else

        try {
            results = service.searchQuery(
                    composeQuery("SELECT * FROM Book ORDER BY book:abstract"),
                    0, 100);
        } catch (QueryException e) {
        }
        // don't catch other exceptions, to get the stack trace
    }
    
 
    
    public void xtestNullDateQuery() throws Exception {
        TestData.createBook(session);
        TestData.create2ndBook(session);
        ResultSet results = service.searchQuery(
                composeQuery("SELECT * FROM Document WHERE book:published = ''"),
                0, 100);
        assertEquals(1, results.size());
    }
    
    
    public void testDateOrderClauses() throws Exception {
        TestData.createBooks(session, 1);
        TestData.create2ndBook(session);
        
        ResultSet results;

        results = service.searchQuery(
                composeQuery("SELECT * FROM Book ORDER BY book:published"),
                0, 100);
        assertEquals(2, results.getTotalHits());
        assertEquals("0002", results.get(0).get("book:barcode"));
        assertEquals("1350000", results.get(1).get("book:barcode"));
        
        results = service.searchQuery(
                composeQuery("SELECT * FROM Book ORDER BY book:published DESC"),
                0, 100);
        assertEquals(2, results.getTotalHits());
        assertEquals("1350000", results.get(0).get("book:barcode"));
        assertEquals("0002", results.get(1).get("book:barcode"));

    }

    
    
    
    
    
    
    
    
    

    private static ComposedNXQuery composeQuery(String query) {
        SQLQuery nxqlQuery = SQLQueryParser.parse(query);
        return new ComposedNXQueryImpl(nxqlQuery);
    }

}
