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
package org.nuxeo.ecm.automation.core.operations.document;

import org.nuxeo.ecm.automation.core.Constants;
import org.nuxeo.ecm.automation.core.annotations.Context;
import org.nuxeo.ecm.automation.core.annotations.Operation;
import org.nuxeo.ecm.automation.core.annotations.OperationMethod;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentRef;
import org.nuxeo.ecm.core.api.security.ACP;

/**
 * Gets ACP of the given document.
 *
 * @author Antoine Taillefer
 */
@Operation(id = GetDocumentACP.ID, category = Constants.CAT_DOCUMENT, label = "Get ACP", description = "Get Acces Control Policy of the input document(s).")
public class GetDocumentACP {

    public static final String ID = "Document.GetACP";

    @Context
    protected CoreSession session;

    @OperationMethod()
    public ACP run(DocumentModel doc) throws Exception {
        return getACP(doc.getRef());
    }

    @OperationMethod()
    public ACP run(DocumentRef docRef) throws Exception {
        return getACP(docRef);
    }

    protected ACP getACP(DocumentRef docRef) throws ClientException {
        return session.getACP(docRef);
    }

}
