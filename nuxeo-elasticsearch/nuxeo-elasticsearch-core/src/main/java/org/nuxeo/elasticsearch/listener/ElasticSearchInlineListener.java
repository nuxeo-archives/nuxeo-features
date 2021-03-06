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
 *     Nuxeo
 */

package org.nuxeo.elasticsearch.listener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Status;
import javax.transaction.Synchronization;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.event.Event;
import org.nuxeo.ecm.core.event.EventListener;
import org.nuxeo.ecm.core.event.impl.DocumentEventContext;
import org.nuxeo.elasticsearch.api.ElasticSearchIndexing;
import org.nuxeo.elasticsearch.commands.IndexingCommand;
import org.nuxeo.elasticsearch.commands.IndexingCommands;
import org.nuxeo.elasticsearch.commands.IndexingCommandsStacker;
import org.nuxeo.runtime.api.Framework;

/**
 * Synchronous Event listener used to schedule a reindexing
 *
 * @author <a href="mailto:tdelprat@nuxeo.com">Tiry</a>
 *
 */
public class ElasticSearchInlineListener extends IndexingCommandsStacker
        implements EventListener, Synchronization {

    private static final Log log = LogFactory.getLog(ElasticSearchInlineListener.class);

    // rely on TransactionManager rather than on Save event
    protected static boolean useTxSync = true;

    protected static ThreadLocal<Map<String, IndexingCommands>> transactionCommands = new ThreadLocal<Map<String, IndexingCommands>>() {
        @Override
        protected HashMap<String, IndexingCommands> initialValue() {
            return new HashMap<String, IndexingCommands>();
        }
    };

    protected static ThreadLocal<Boolean> synched = new ThreadLocal<Boolean>() {
        @Override
        protected Boolean initialValue() {
            return Boolean.FALSE;
        }
    };

    @Override
    protected Map<String, IndexingCommands> getAllCommands() {
        return transactionCommands.get();
    }

    @Override
    public void handleEvent(Event event) throws ClientException {

        String eventId = event.getName();

        // manual flush on save if TxManager is not hooked
        if (event.isCommitEvent()) {
            if (!synched.get()) {
                flushCommands();
            }
            return;
        }

        DocumentEventContext docCtx;
        if (event.getContext() instanceof DocumentEventContext) {
            docCtx = (DocumentEventContext) event.getContext();
        } else {
            // don't process Events that are not tied to Documents
            return;
        }
        stackCommand(docCtx, eventId);

        // register via Tx Manager
        if (!synched.get() && useTxSync) {
            synched.set(registerSynchronization(this));
        }

    }

    protected void stackCommand(DocumentEventContext docCtx, String eventId) {

        if (Framework.isTestModeSet() && Framework.getRuntime().isShuttingDown()) {
            // avoid processing events generated by the shutdown process
            return;
        }

        DocumentModel doc = docCtx.getSourceDocument();

        if (doc == null) {
            return;
        }

        Boolean block = (Boolean) docCtx.getProperty(EventConstants.DISABLE_AUTO_INDEXING);
        if (block != null && block) {
            // ignore the event - we are blocked by the caller
            if (log.isDebugEnabled()) {
                log.debug(
                        "Skip indexing for doc " + docCtx.getSourceDocument());
            }
            return;
        }

        Boolean sync = (Boolean) docCtx.getProperty(EventConstants.ES_SYNC_INDEXING_FLAG);
        if (sync == null) {
            sync = (Boolean) doc.getContextData().get(
                    EventConstants.ES_SYNC_INDEXING_FLAG);
        }

        if (sync == null) {
            sync = useSyncIndexing.get();
            if (sync == null) {
                sync = false;
            }
        }

        stackCommand(doc, eventId, sync);
    }

    @Override
    protected void fireSyncIndexing(List<IndexingCommand> syncCommands)
            throws ClientException {
        ElasticSearchIndexing esi = Framework.getLocalService(ElasticSearchIndexing.class);
        for (IndexingCommand cmd : syncCommands) {
            esi.scheduleIndexing(cmd);
        }
    }

    @Override
    protected void fireAsyncIndexing(List<IndexingCommand> asyncCommands)
            throws ClientException {
        ElasticSearchIndexing esi = Framework.getLocalService(ElasticSearchIndexing.class);
        for (IndexingCommand cmd : asyncCommands) {
            esi.scheduleIndexing(cmd);
        }
    }

    @Override
    public void beforeCompletion() {
        try {
            // run flush !
            flushCommands();
        } catch (Exception e) {
            log.error("Error during flush", e);
        } finally {
            synched.set(false);
        }
    }

    @Override
    public void afterCompletion(int status) {
        if (Status.STATUS_MARKED_ROLLBACK == status
                || Status.STATUS_ROLLEDBACK == status) {
            synched.set(false);
            getAllCommands().clear();
        }
        useSyncIndexing.set(null);
    }

    public static ThreadLocal<Boolean> useSyncIndexing = new ThreadLocal<Boolean>() {
        @Override
        protected Boolean initialValue() {
            return Boolean.FALSE;
        }

        @Override
        public void set(Boolean value) {
            super.set(value);
            if (Boolean.TRUE.equals(value)) {
                // switch existing stack to sync
                for (IndexingCommands cmds : transactionCommands.get().values()) {
                    for (IndexingCommand cmd : cmds.getCommands()) {
                        cmd.makeSync();
                    }
                }
            }
        }

    };

}
