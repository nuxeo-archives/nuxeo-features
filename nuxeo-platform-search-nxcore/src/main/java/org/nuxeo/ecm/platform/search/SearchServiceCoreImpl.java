package org.nuxeo.ecm.platform.search;

import java.io.Serializable;
import java.security.Principal;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.api.NuxeoPrincipal;
import org.nuxeo.ecm.core.api.repository.Repository;
import org.nuxeo.ecm.core.api.repository.RepositoryManager;
import org.nuxeo.ecm.core.api.repository.cache.CachingRepositoryInstanceHandler;
import org.nuxeo.ecm.core.api.security.SecurityConstants;
import org.nuxeo.ecm.core.search.api.backend.indexing.resources.ResolvedResources;
import org.nuxeo.ecm.core.search.api.client.IndexingException;
import org.nuxeo.ecm.core.search.api.client.SearchException;
import org.nuxeo.ecm.core.search.api.client.SearchService;
import org.nuxeo.ecm.core.search.api.client.indexing.blobs.BlobExtractor;
import org.nuxeo.ecm.core.search.api.client.indexing.resources.IndexableResources;
import org.nuxeo.ecm.core.search.api.client.indexing.session.SearchServiceSession;
import org.nuxeo.ecm.core.search.api.client.query.ComposedNXQuery;
import org.nuxeo.ecm.core.search.api.client.query.NativeQuery;
import org.nuxeo.ecm.core.search.api.client.query.NativeQueryString;
import org.nuxeo.ecm.core.search.api.client.query.QueryException;
import org.nuxeo.ecm.core.search.api.client.query.SearchPrincipal;
import org.nuxeo.ecm.core.search.api.client.query.impl.SearchPrincipalImpl;
import org.nuxeo.ecm.core.search.api.client.search.results.ResultSet;
import org.nuxeo.ecm.core.search.api.client.search.results.impl.ResultItemImpl;
import org.nuxeo.ecm.core.search.api.client.search.results.impl.ResultSetImpl;
import org.nuxeo.ecm.core.search.api.events.IndexingEventConf;
import org.nuxeo.ecm.core.search.api.indexing.resources.configuration.IndexableResourceConf;
import org.nuxeo.ecm.core.search.api.indexing.resources.configuration.ResourceTypeDescriptor;
import org.nuxeo.ecm.core.search.api.indexing.resources.configuration.document.FulltextFieldDescriptor;
import org.nuxeo.ecm.core.search.api.indexing.resources.configuration.document.IndexableDocType;
import org.nuxeo.runtime.api.Framework;
import org.nuxeo.runtime.model.DefaultComponent;

public class SearchServiceCoreImpl extends DefaultComponent implements SearchService{

    private static final String NOT_IMPLEMENTED = "Not implemented by NXcore search service";

    private static final Log log = LogFactory.getLog(SearchServiceCoreImpl.class);

    CoreSession session;
    /**
     *
     */
    private static final long serialVersionUID = -1924269550015564634L;

    public void clear() throws IndexingException {
        throw new RuntimeException(NOT_IMPLEMENTED);
    }

    public void closeSession(String sid) {
        throw new RuntimeException(NOT_IMPLEMENTED);
    }

    public void deleteAggregatedResources(String key) throws IndexingException {
        log.warn("SearchServiceCoreImpl.deleteAggregatedResources(String key) " + NOT_IMPLEMENTED);
    }

    public void deleteAtomicResource(String key) throws IndexingException {
        log.warn("SearchServiceCoreImpl.deleteAtomicResource(String key) " + NOT_IMPLEMENTED);
    }

    public int getActiveIndexingTasks() {
        throw new RuntimeException(NOT_IMPLEMENTED);
//      return 0;
    }

    public String[] getAvailableBackendNames() {
        throw new RuntimeException(NOT_IMPLEMENTED);
//      return null;
    }

    public BlobExtractor getBlobExtractorByName(String name) {
        throw new RuntimeException(NOT_IMPLEMENTED);
//      return null;
    }

    public FulltextFieldDescriptor getFullTextDescriptorByName(
            String prefixedName) {
        throw new RuntimeException(NOT_IMPLEMENTED);
//      return null;
    }

    public IndexableDocType getIndexableDocTypeFor(String docType) {
        throw new RuntimeException(NOT_IMPLEMENTED);
//      return null;
    }

    public IndexableResourceConf getIndexableResourceConfByName(String name,
            boolean full) {
        throw new RuntimeException(NOT_IMPLEMENTED);
//      return null;
    }

    public IndexableResourceConf getIndexableResourceConfByPrefix(
            String prefix, boolean full) {
        throw new RuntimeException(NOT_IMPLEMENTED);
//      return null;
    }

    public Map<String, IndexableResourceConf> getIndexableResourceConfs() {
        throw new RuntimeException(NOT_IMPLEMENTED);
//      return null;
    }

    public int getIndexingDocBatchSize() {
        throw new RuntimeException(NOT_IMPLEMENTED);
//      return 0;
    }

    public IndexingEventConf getIndexingEventConfByName(String name) {
        throw new RuntimeException(NOT_IMPLEMENTED);
//      return null;
    }

    public long getIndexingWaitingQueueSize() {
        throw new RuntimeException(NOT_IMPLEMENTED);
//      return 0;
    }

    public int getNumberOfIndexingThreads() {
        throw new RuntimeException(NOT_IMPLEMENTED);
//      return 0;
    }

    public ResourceTypeDescriptor getResourceTypeDescriptorByName(String name) {
        throw new RuntimeException(NOT_IMPLEMENTED);
//      return null;
    }

    public List<String> getSupportedAnalyzersFor(String backendName) {
        throw new RuntimeException(NOT_IMPLEMENTED);
//      return null;
    }

    public List<String> getSupportedFieldTypes(String backendName) {
        throw new RuntimeException(NOT_IMPLEMENTED);
//      return null;
    }

    public long getTotalCompletedIndexingTasks() {
        throw new RuntimeException(NOT_IMPLEMENTED);
//      return 0;
    }

    public void index(ResolvedResources sources) throws IndexingException {
        log.warn("SearchServiceCoreImpl.index(ResolvedResources sources) " + NOT_IMPLEMENTED);
    }

    public void index(IndexableResources sources, boolean fulltext) throws IndexingException {
        log.warn("SearchServiceCoreImpl.index(IndexableResources sources, boolean fulltext) " + NOT_IMPLEMENTED);
    }

    public void indexInThread(ResolvedResources sources)
    throws IndexingException {
        throw new RuntimeException(NOT_IMPLEMENTED);

    }

    public void indexInThread(DocumentModel dm, Boolean recursive,
            boolean fulltext) throws IndexingException {
        throw new RuntimeException(NOT_IMPLEMENTED);

    }

    public void invalidateComputedIndexableResourceConfs() {
        throw new RuntimeException(NOT_IMPLEMENTED);

    }

    public boolean isEnabled() {
        throw new RuntimeException(NOT_IMPLEMENTED);
//      return false;
    }

    public boolean isReindexingAll() {
        throw new RuntimeException(NOT_IMPLEMENTED);
//      return false;
    }

    public SearchServiceSession openSession() {
        throw new RuntimeException(NOT_IMPLEMENTED);
//      return null;
    }

    public void reindexAll(String repoName, String path, boolean fulltext)
    throws IndexingException {
        throw new RuntimeException(NOT_IMPLEMENTED);

    }

    public void saveAllSessions() throws IndexingException {
        throw new RuntimeException(NOT_IMPLEMENTED);

    }


    protected CoreSession getSession(){
        if ( session == null ) {
            try {
                RepositoryManager manager = Framework.getService(RepositoryManager.class);
                Map<String, Serializable> ctx = new HashMap<String, Serializable>();
                ctx.put("username", SecurityConstants.ADMINISTRATOR);
                session = manager.getDefaultRepository().open(ctx);
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
        return session;
    }

    public ResultSet searchQuery(ComposedNXQuery nxqlQuery, int offset,
            int range) throws SearchException, QueryException {
        CoreSession session = getSession();
        try {
            String query = nxqlQuery.getQuery().toString();
            // hack to bad query
            query = query.replace("ecm:currentLifeCycleState", "ecm:currentLifecycleState");
            DocumentModelList list = session.query(query);
            
            
            int size = list.size();
            int pageHits = 0;
            if ( size > offset + range ){
                pageHits = range;
            } else {
                pageHits = size - offset;
            }
            
            
            return new ResultSetImpl(nxqlQuery.getQuery(), nxqlQuery.getSearchPrincipal(),offset,range,list, list.size(), pageHits );
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ResultSet searchQuery(NativeQuery nativeQuery, int offset, int range)
    throws SearchException, QueryException {
        throw new RuntimeException(NOT_IMPLEMENTED);
//      return null;
    }

    public ResultSet searchQuery(NativeQueryString queryString,
            String backendName, int offset, int range) throws SearchException,
            QueryException {
        throw new RuntimeException(NOT_IMPLEMENTED);
//      return null;
    }

    public void setIndexingDocBatchSize(int docBatchSize) {
        throw new RuntimeException(NOT_IMPLEMENTED);

    }

    public void setNumberOfIndexingThreads(int numberOfIndexingThreads) {
        throw new RuntimeException(NOT_IMPLEMENTED);

    }

    public void setReindexingAll(boolean flag) {
        throw new RuntimeException(NOT_IMPLEMENTED);

    }

    public void setStatus(boolean active) {
        throw new RuntimeException(NOT_IMPLEMENTED);

    }

    // copied from nuxeo platform search core
    public final SearchPrincipal getSearchPrincipal(Principal principal) {
        if (principal == null) {
            return null;
        }
        String name = principal.getName();
        // :FIXME: find a better way to find this out. For now this is the
        // only available way of doing it.
        boolean systemUser = name.equals("system");

        String[] groups;
        if (principal instanceof NuxeoPrincipal) {
            NuxeoPrincipal nuxeoPrincipal = (NuxeoPrincipal) principal;
            // security checks are done on the transitive closure of group
            // membership
            groups = nuxeoPrincipal.getAllGroups().toArray(
                    new String[nuxeoPrincipal.getAllGroups().size() + 1]);
            groups[groups.length - 1] = SecurityConstants.EVERYONE;
        } else {
            // decided not to add EVERYONE because that's a Nuxeo concept
            // TODO adapt when we have real use cases of this
            groups = new String[0];
        }
        return new SearchPrincipalImpl(name, groups, systemUser, principal);
    }



}
