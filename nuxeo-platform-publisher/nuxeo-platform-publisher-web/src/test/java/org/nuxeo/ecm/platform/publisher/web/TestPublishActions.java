package org.nuxeo.ecm.platform.publisher.web;

import junit.framework.Assert;

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
import org.nuxeo.runtime.test.runner.Deploy;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;
import org.nuxeo.runtime.test.runner.LocalDeploy;

import com.google.inject.Inject;

/**
 * 
 * @author mkalam-alami
 *
 */
@SuppressWarnings("deprecation")
@RunWith(FeaturesRunner.class)
@Features(CoreFeature.class)
@Deploy("org.nuxeo.ecm.platform.publisher.web")
@LocalDeploy("org.nuxeo.ecm.platform.publisher.web:OSGI-INF/core-types-contrib.xml")
@RepositoryConfig(init = DefaultRepositoryInit.class, user = "Administrator", cleanup = Granularity.METHOD)
public class TestPublishActions {
    
    @Inject
    private CoreSession documentManager;

    @Inject
    private ResourcesAccessorBean resourcesAccessor;

    @Test(timeout = 3000) // In case an infinite loop occurs
    public void testGetPathFragments() throws ClientException {
        // Create file in standard domain
        DocumentModel fileModel = documentManager.createDocumentModel("/default-domain/workspaces/", "myfile", "File");
        fileModel = documentManager.createDocument(fileModel);

        // Create file in custom domain with specific type
        DocumentModel customDomainModel = documentManager.createDocumentModel("/", "mydomain", "CustomDomain");
        customDomainModel = documentManager.createDocument(customDomainModel);
        DocumentModel file2Model = documentManager.createDocumentModel(customDomainModel.getPathAsString(), "myfile2", "File");
        file2Model = documentManager.createDocument(file2Model);
        
        // Create file directly below the root
        DocumentModel file3Model = documentManager.createDocumentModel("/", "myfile3", "File");
        file3Model = documentManager.createDocument(file3Model);
        
        // Test paths (every fragment is null because the message map is empty)
        DummyPublishActions publishActions = new DummyPublishActions(documentManager, resourcesAccessor);
        Assert.assertEquals("null>null>null", publishActions.getFormattedPath(fileModel));
        Assert.assertEquals("null>null", publishActions.getFormattedPath(file2Model));
        Assert.assertEquals("null>null", publishActions.getFormattedPath(file3Model));
    }
    
}
