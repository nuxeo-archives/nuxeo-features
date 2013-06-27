/*
 * Copyright (c) 2006-2011 Nuxeo SA (http://nuxeo.com/) and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     bstefanescu
 */
package org.nuxeo.ecm.automation.core.test;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transaction;

import org.nuxeo.ecm.automation.OperationContext;
import org.nuxeo.ecm.automation.core.annotations.Context;
import org.nuxeo.ecm.automation.core.annotations.Operation;
import org.nuxeo.ecm.automation.core.annotations.OperationMethod;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.PathRef;
import org.nuxeo.runtime.transaction.TransactionHelper;

/**
 *
 * @author <a href="mailto:tdelprat@nuxeo.com">Tiry</a>
 *
 */
@Operation(id = "runOnListItemWithTx")
public class RunOnListItemWithTx {

    @Context
    OperationContext ctx;

    @Context
    CoreSession session;

    protected List<String> getOrCreateList(String name) {
        @SuppressWarnings("unchecked")
		List<String> list = (List<String>) ctx.get(name);
        if (list==null) {
            list = new ArrayList<String>();
            ctx.put(name, list);
        }
        return list;
    }

    @OperationMethod
    public void printInfo() throws Exception {
    		DocumentModel folder = session.getDocument(new PathRef("/default-domain/workspaces/test"));
    		folder.getProperty("dc:issued");
    		String user = (String) ctx.get("item");
    		DocumentModel userFolder = session.createDocumentModel(folder.getPathAsString(), user, "Folder");
    		userFolder = session.createDocument(userFolder);
        Transaction tx = TransactionHelper.lookupTransactionManager().getTransaction();
        getOrCreateList("result").add(user);
        getOrCreateList("docids").add(userFolder.getId());
        getOrCreateList("txids").add(tx.toString());
    }

}
