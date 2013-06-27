/*
 * (C) Copyright 2006-2013 Nuxeo SAS (http://nuxeo.com/) and contributors.
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
 * Contributors:
 *     Nuxeo - initial API and implementation
 *
 */

package org.nuxeo.ecm.automation.core.operations.execution;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.automation.AutomationService;
import org.nuxeo.ecm.automation.OperationContext;
import org.nuxeo.ecm.automation.OperationException;
import org.nuxeo.ecm.automation.core.Constants;
import org.nuxeo.ecm.automation.core.annotations.Context;
import org.nuxeo.ecm.automation.core.annotations.Operation;
import org.nuxeo.ecm.automation.core.annotations.OperationMethod;
import org.nuxeo.ecm.automation.core.annotations.Param;
import org.nuxeo.ecm.core.api.AbstractSession;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.TransactionalCoreSessionWrapper;
import org.nuxeo.ecm.core.model.Session;
import org.nuxeo.runtime.transaction.TransactionHelper;

/**
 * Run an embedded operation chain inside separated transactions using the
 * current input. The output is undefined (Void)
 *
 * @since 5.7
 * @author <a href="mailto:tdelprat@nuxeo.com">Tiry</a>
 *
 */
@Operation(id = RunOperationOnListInNewTransaction.ID, category = Constants.CAT_SUBCHAIN_EXECUTION, label = "Run For Each in new TX", description = "Run an operation in a new Transaction for each element from the list defined by the 'list' paramter. The 'list' parameter is pointing to context variable that represent the list which will be iterated. The 'item' parameter represent the name of the context varible which will point to the current element in the list at each iteration. You can use the 'isolate' parameter to specify whether or not the evalution context is the same as the parent context or a copy of it. If the isolate is 'true' then a copy of the current contetx is used and so that modifications in this context will not affect the parent context. Any input is accepted. The input is returned back as output when operation terminate.")
public class RunOperationOnListInNewTransaction {

    protected static Log log = LogFactory.getLog(RunOperationOnListInNewTransaction.class);

    public static final String ID = "Context.RunOperationOnListInNewTx";

    @Context
    protected OperationContext ctx;

    @Context
    protected AutomationService service;

    @Param(name = "id")
    protected String chainId;

    @Param(name = "list")
    protected String listName;

    @Param(name = "item", required = false, values = "item")
    protected String itemName = "item";

    @Param(name = "fetchDocuments", required = false, values = "true")
    protected boolean fetchDocuments = true;

    @Param(name = "isolate", required = false, values = "true")
    protected boolean isolate = true;

    @OperationMethod
    public void run() throws Exception {

        Map<String, Object> vars = new HashMap<String, Object>();

        final CoreSession session = ctx.getCoreSession();

        final AbstractSession coreSession = unwrap(session);

        Collection<?> list = null;
        if (ctx.get(listName) instanceof Object[]) {
            list = Arrays.asList((Object[]) ctx.get(listName));
        } else if (ctx.get(listName) instanceof Collection<?>) {
            list = (Collection<?>) ctx.get(listName);
        } else {
            throw new UnsupportedOperationException(
                    ctx.get(listName).getClass() + " is not a Collection");
        }

        dispose(coreSession);

        // execute in sub transactions
        for (Object value : list) {
            boolean succeed = false;
            try {
                OperationContext subctx = new OperationContext(session, vars);
                subctx.setInput(ctx.getInput());
                subctx.put(itemName, value);
                service.run(subctx, chainId);
                succeed = true;
            } finally {
                if (!succeed) {
                    TransactionHelper.setTransactionRollbackOnly();
                }
                dispose(coreSession);
            }
        }


        if (!isolate) {
            for (String varName : vars.keySet()) {
                if (!ctx.getVars().containsKey(varName)) {
                    ctx.put(varName, vars.get(varName));
                } else {
                    Object value = vars.get(varName);
                    if (value != null && value instanceof DocumentModel) {
                        ctx.getVars().put(
                                varName,
                                coreSession.getDocument(((DocumentModel) value).getRef()));
                    } else {
                        ctx.getVars().put(varName, value);
                    }
                }
            }
        }

    }

    protected void dispose(AbstractSession session) throws OperationException {
        Session repoSession;
        try {
            repoSession = session.getSession();
        } catch (ClientException e) {
            throw new OperationException("Cannot access to repo session from "
                    + session.getClass());
        }
        try {
            TransactionHelper.commitOrRollbackTransaction();
        } finally {
            repoSession.dispose();
            TransactionHelper.startTransaction();
        }
    }

    protected static Field wrappedSession = loadWrappedSessionField();

    protected static Field loadWrappedSessionField() {
        Field field;
        try {
            field = TransactionalCoreSessionWrapper.class.getDeclaredField("session");
        } catch (NoSuchFieldException | SecurityException e) {
            throw new Error("Cannot get access to wrapped session field of "
                    + TransactionalCoreSessionWrapper.class.getSimpleName(), e);
        }
        field.setAccessible(true);
        return field;
    }

    protected AbstractSession unwrap(CoreSession session)
            throws OperationException {
        Class<? extends CoreSession> clazz = session.getClass();
        if (AbstractSession.class.isAssignableFrom(clazz)) {
            return (AbstractSession) session;
        }
        if (Proxy.isProxyClass(clazz)) {
            InvocationHandler handler = Proxy.getInvocationHandler(session);
            if (!(handler instanceof TransactionalCoreSessionWrapper)) {
                throw new OperationException("Unsupported session proxy "
                        + handler.getClass());
            }
            try {
                return (AbstractSession) wrappedSession.get(handler);
            } catch (IllegalArgumentException | IllegalAccessException e) {
                throw new OperationException(
                        "Cannot unwrap core session proxy", e);
            }
        }
        throw new OperationException("Unknown core session " + clazz
                + " , cannot dispose");
    }
}
