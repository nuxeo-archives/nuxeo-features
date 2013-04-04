package org.nuxeo.ecm.platform.publisher.web;

import junit.framework.Assert;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.test.CoreFeature;
import org.nuxeo.ecm.core.test.DefaultRepositoryInit;
import org.nuxeo.ecm.core.test.annotations.Granularity;
import org.nuxeo.ecm.core.test.annotations.RepositoryConfig;
import org.nuxeo.ecm.webapp.helpers.ResourcesAccessorBean;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;

import com.google.inject.Inject;

/**
 * 
 * @author mkalam-alami
 *
 */
@SuppressWarnings("deprecation")
@RunWith(FeaturesRunner.class)
@Features(CoreFeature.class)
@RepositoryConfig(init = DefaultRepositoryInit.class, user = "Administrator", cleanup = Granularity.METHOD)
public class TestPublishActions {
    
    @Inject
    private CoreSession documentManager;

    @Inject
    private ResourcesAccessorBean resourcesAccessor;

    @Test(timeout = 3000) // In case an infinite loop occurs
    public void testGetPathFragments() throws ClientException {
        // Create files
        DocumentModel fileModel = documentManager.createDocumentModel("/default-domain/workspaces/", "myfile", "File");
        fileModel = documentManager.createDocument(fileModel);

        DocumentModel file2Model = documentManager.createDocumentModel("/", "myfile2", "File");
        file2Model = documentManager.createDocument(file2Model);
        
        // Test paths (every fragment is null because the message map is empty)
        DummyPublishActions publishActions = new DummyPublishActions(documentManager, resourcesAccessor);
        Assert.assertEquals("null>null>null", publishActions.getFormattedPath(fileModel));
        Assert.assertEquals("null", publishActions.getFormattedPath(file2Model));
    }
    
}
