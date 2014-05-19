/*
 * (C) Copyright 2014 Nuxeo SA (http://nuxeo.com/) and contributors.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser General Public License
 * (LGPL) version 2.1 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl-2.1.html
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * Contributors:
 *     Antoine Taillefer <ataillefer@nuxeo.com>
 */
package org.nuxeo.ecm.automation.core;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.jaxrs.impl.HttpAutomationClient;
import org.nuxeo.ecm.automation.core.operations.document.GetDocumentACP;
import org.nuxeo.ecm.automation.core.operations.document.SetDocumentACE;
import org.nuxeo.ecm.automation.test.EmbeddedAutomationServerFeature;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.security.ACP;
import org.nuxeo.ecm.core.api.security.SecurityConstants;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;
import org.nuxeo.runtime.test.runner.Jetty;

import com.google.inject.Inject;

/**
 * Tests the {@link GetDocumentACP} operation.
 *
 * @author Antoine Taillefer
 */
@RunWith(FeaturesRunner.class)
@Features(EmbeddedAutomationServerFeature.class)
@Jetty(port = 18080)
public class TestGetDocumentACP {

    @Inject
    protected CoreSession session;

    @Inject
    protected HttpAutomationClient automationClient;

    protected Session clientSession;

    @Before
    public void init() throws Exception {

        // Get an Automation client session as Administrator
        clientSession = automationClient.getSession("Administrator",
                "Administrator");
    }

    @Test
    public void testDetDocumentACP() throws Exception {

        // Create a document
        DocumentModel doc = session.createDocumentModel("/", "testDoc",
                "Folder");
        doc = session.createDocument(doc);
        session.save();

        // Get document ACP
        ACP acp = (ACP) clientSession.newRequest(GetDocumentACP.ID).setInput(
                doc.getPathAsString()).execute();

        // Update document ACP
        clientSession.newRequest(SetDocumentACE.ID).setInput(
                doc.getPathAsString()).set("user", "Guest").set("permission",
                SecurityConstants.READ_WRITE).set("grant", true).execute();

        // Get document ACP
        acp = (ACP) clientSession.newRequest(GetDocumentACP.ID).setInput(
                doc.getPathAsString()).execute();
    }

}
