/*
 * Copyright (c) 2006-2013 Nuxeo SA (http://nuxeo.com/) and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Vladimir Pasquier <vpasquier@nuxeo.com>
 *     Antoine Taillefer <ataillefer@nuxeo.com>
 *
 */
package org.nuxeo.ecm.platform.picture.api.thumbnail.factories;

import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentRef;
import org.nuxeo.ecm.platform.picture.api.thumbnail.ThumbnailFactory;
import org.nuxeo.ecm.platform.picture.api.thumbnail.adapters.ThumbnailAdapter;

/**
 * Default thumbnail factory for all folderish documents
 * 
 * @since 5.7
 */
public class ThumbnailFolderishFactory implements ThumbnailFactory {

    @Override
    public Blob getThumbnail(DocumentModel doc, CoreSession session) throws ClientException {
        if (!doc.isFolder()) {
            throw new ClientException("Document is not folderish");
        }
        DocumentRef docRef = doc.getRef();
        // TODO: Choose which rules apply for both cases (use pageprovider for
        // getting real "first child")
        if (session.hasChildren(docRef)) {
            return session.getChildren(docRef).get(0).getAdapter(
                    ThumbnailAdapter.class).getThumbnail(session);
        }
        return null;
    }
}
