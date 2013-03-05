/*
 * (C) Copyright 2006-2012 Nuxeo SAS (http://nuxeo.com/) and contributors.
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
 *
 */

package org.nuxeo.ecm.platform.audit;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.LifeCycleConstants;
import org.nuxeo.ecm.core.api.event.DocumentEventTypes;
import org.nuxeo.ecm.core.api.impl.UserPrincipal;
import org.nuxeo.ecm.core.event.EventService;
import org.nuxeo.ecm.core.test.RepositorySettings;
import org.nuxeo.ecm.core.test.TestRepositoryHandler;
import org.nuxeo.ecm.platform.audit.api.AuditReader;
import org.nuxeo.ecm.platform.audit.api.LogEntry;
import org.nuxeo.runtime.api.Framework;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;
import org.nuxeo.runtime.test.runner.LocalDeploy;
import org.nuxeo.runtime.test.runner.LogCaptureFeature;
import org.nuxeo.runtime.transaction.TransactionHelper;

import com.google.inject.Inject;

@RunWith(FeaturesRunner.class)
@Features({AuditFeature.class, LogCaptureFeature.class})
@LogCaptureFeature.With(LogCaptureFeature.FilterErrors.class)
@LocalDeploy("org.nuxeo.ecm.platform.audit:test-audit-contrib.xml")
public class TestTransactedAudit {

    protected @Inject LogCaptureFeature.Result logResults;

    protected @Inject CoreSession repo;

    protected @Inject RepositorySettings settings;

    @Before public void isInjected() {
        assertThat(repo, notNullValue());
    }

    @Test public void testConcurrency() throws ClientException {
        TransactionHelper.commitOrRollbackTransaction();
        TestRepositoryHandler handler = settings.getRepositoryHandler();
        UserPrincipal principal = new UserPrincipal("test",
                new ArrayList<String>(), false, true);
        Map<String,Serializable> context = new HashMap<String,Serializable>();
        context.put("username", "test");
        context.put("principal", principal);
        Framework.getLocalService(EventService.class).waitForAsyncCompletion();
        for (int i = 0; i < 100; ++i) {
            TransactionHelper.startTransaction();
            try {
                CoreSession session = handler.openSession(context);
                try {
                    DocumentModel doc = session.createDocumentModel("/", "a-file-" + i, "File");
                    doc = session.createDocument(doc);
                } finally {
                    handler.releaseSession(session);
                }
            } finally {
                TransactionHelper.commitOrRollbackTransaction();
            }
        }
        Framework.getLocalService(EventService.class).waitForAsyncCompletion();
        assertEquals(0, logResults.getCaughtEvents().size());
    }

    @Test public void canLogMultipleLifecycleTransitionsInSameTx() throws ClientException {
        // generate events
        DocumentModel doc = repo.createDocumentModel("/", "a-file", "File");
        doc = repo.createDocument(doc);
        String initialLifeCycle = doc.getCurrentLifeCycleState();
        doc.followTransition(LifeCycleConstants.DELETE_TRANSITION);
        String deletedLifeCycle = doc.getCurrentLifeCycleState();
        doc.followTransition(LifeCycleConstants.UNDELETE_TRANSITION);
        String undeletedLifeCycle = doc.getCurrentLifeCycleState();
        TransactionHelper.commitOrRollbackTransaction();
        Framework.getLocalService(EventService.class).waitForAsyncCompletion();

        // test audit trail
        AuditReader reader = Framework.getLocalService(AuditReader.class);
        List<LogEntry> trail = reader.getLogEntriesFor(doc.getId());

        assertThat(trail, notNullValue());
        assertThat(trail.size(), is(3));

        boolean seenDocCreated = false;
        boolean seenDocDeleted = false;
        boolean seenDocUndeleted = false;

        for (LogEntry entry:trail) {
            String lifeCycle = entry.getDocLifeCycle();
            String id = entry.getEventId();
            if (DocumentEventTypes.DOCUMENT_CREATED.equals(id)) {
                if (initialLifeCycle.equals(lifeCycle)) {
                    seenDocCreated = true;
                }
            } else if (LifeCycleConstants.TRANSITION_EVENT.equals(id)) {
                if (undeletedLifeCycle.equals(lifeCycle)) {
                    seenDocUndeleted = true;
                } else if (deletedLifeCycle.equals(lifeCycle)) {
                    seenDocDeleted = true;
                }
            }
        }

        assertThat(seenDocUndeleted, is(true));
        assertThat(seenDocDeleted, is(true));
        assertThat(seenDocCreated, is(true));
    }

}
