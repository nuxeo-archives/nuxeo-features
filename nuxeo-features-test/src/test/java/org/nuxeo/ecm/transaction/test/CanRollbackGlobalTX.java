package org.nuxeo.ecm.transaction.test;

import java.util.Collections;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentRef;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.core.test.CoreFeature;
import org.nuxeo.ecm.core.test.RepositorySettings;
import org.nuxeo.ecm.core.test.TestRepositoryHandler;
import org.nuxeo.ecm.core.test.annotations.TransactionalConfig;
import org.nuxeo.ecm.directory.Directory;
import org.nuxeo.ecm.directory.DirectoryException;
import org.nuxeo.ecm.directory.Session;
import org.nuxeo.ecm.directory.sql.SQLDirectoryFeature;
import org.nuxeo.ecm.platform.audit.AuditFeature;
import org.nuxeo.ecm.platform.audit.api.LogEntry;
import org.nuxeo.ecm.platform.audit.api.Logs;
import org.nuxeo.runtime.jtajca.management.JtajcaManagementFeature;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;
import org.nuxeo.runtime.transaction.TransactionHelper;

import com.google.inject.Inject;
import com.google.inject.name.Named;

@RunWith(FeaturesRunner.class)
@Features({ JtajcaManagementFeature.class, CoreFeature.class,
        AuditFeature.class, SQLDirectoryFeature.class })
@TransactionalConfig(autoStart=false)
public class CanRollbackGlobalTX {

    @Named("users")
    @Inject
    Directory users;

    @Inject
    RepositorySettings repository;

    @Inject
    Logs audit;


    String userId;
    DocumentRef docRef;
    long logId;

    @Test
    public void canRollback() throws ClientException {
        TransactionHelper.startTransaction();
        try {
            injectJdoe();
        } finally {
            TransactionHelper.setTransactionRollbackOnly();
            TransactionHelper.commitOrRollbackTransaction();
        }
        TransactionHelper.startTransaction();
        try {
            checkJdoe();
        } finally {
            TransactionHelper.commitOrRollbackTransaction();
        }
    }

    protected void injectJdoe() throws ClientException {
        userId = createJDoeUser();
        docRef = addJdoeDocument();
        logId  = logJdoe();
    }

    protected void checkJdoe() throws ClientException {
        assertUserNotExist();
        assertDocumentDoesNotExist();
        assertLogNotExist();
    }

    protected DocumentRef addJdoeDocument() throws ClientException {
        TestRepositoryHandler handler = repository.getRepositoryHandler();
        CoreSession session = repository.createSession();
        try {
            DocumentModel jdoeModel = session.createDocumentModel("/", "jdoe",
                    "Document");
            jdoeModel = session.createDocument(jdoeModel);
            session.save();
            return new IdRef(jdoeModel.getId());
        } finally {
            handler.releaseSession(session);
        }
    }

    protected void assertDocumentDoesNotExist() throws ClientException {
        TestRepositoryHandler handler = repository.getRepositoryHandler();
        CoreSession session = repository.createSession();
        try {
            boolean docExists = session.exists(docRef);
            Assert.assertThat(docExists, Matchers.is(false));
        } finally {
            handler.releaseSession(session);
        }
    }

    protected long logJdoe() {
        LogEntry entry = audit.newLogEntry();
        entry.setEventId("jdoe");
        entry.setCategory("users");
        entry.setComment("jdoe creation");
        audit.addLogEntries(Collections.singletonList(entry));
        return entry.getId();
    }

    protected void assertLogNotExist() {
        LogEntry entry = audit.getLogEntryByID(logId);
        Assert.assertThat(entry, Matchers.nullValue());
    }

    protected String createJDoeUser() throws ClientException {
        Session session = users.getSession();
        try {
            DocumentModel jdoeModel = session.getEntry("Administrator");
            jdoeModel.setPropertyValue("user:username", "jdoe");
            jdoeModel.setPropertyValue("user:password", "jdoe");
            jdoeModel = session.createEntry(jdoeModel);
            return jdoeModel.getId();
        } finally {
            session.close();
        }
    }

    protected void assertUserNotExist() throws DirectoryException {
        Session session = users.getSession();
        try {
            DocumentModel jdoeModel = session.getEntry("jdoe");
            Assert.assertThat(jdoeModel, Matchers.nullValue());
        } finally {
            session.close();
        }
    }
}
