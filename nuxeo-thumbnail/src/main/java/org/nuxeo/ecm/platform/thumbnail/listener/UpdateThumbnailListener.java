/*
 * Copyright (c) 2006-2013 Nuxeo SA (http://nuxeo.com/) and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Vladimir Pasquier <vpasquier@nuxeo.com>
 * Laurent Doguin <ldoguin@nuxeo.com>
 */
package org.nuxeo.ecm.platform.thumbnail.listener;

import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.blobholder.BlobHolder;
import org.nuxeo.ecm.core.api.event.DocumentEventTypes;
import org.nuxeo.ecm.core.event.Event;
import org.nuxeo.ecm.core.event.EventBundle;
import org.nuxeo.ecm.core.event.EventContext;
import org.nuxeo.ecm.core.event.PostCommitEventListener;
import org.nuxeo.ecm.core.event.impl.DocumentEventContext;
import org.nuxeo.ecm.platform.thumbnail.AddThumbnailUnrestricted;
import org.nuxeo.ecm.platform.thumbnail.ThumbnailConstants;

/**
 * Thumbnail listener handling creation and update document event to store doc
 * thumbnail
 * 
 * @since 5.7
 */
public class UpdateThumbnailListener implements PostCommitEventListener {

    public void handleEvent(Event event) throws ClientException {
        EventContext ec = event.getContext();
        if (ec instanceof DocumentEventContext) {
            DocumentEventContext context = (DocumentEventContext) ec;
            DocumentModel doc = context.getSourceDocument();
            if (!doc.getType().equals("File"))
                return;
            BlobHolder blobHolder = doc.getAdapter(BlobHolder.class);
            if (blobHolder != null) {
                Blob blob = blobHolder.getBlob();
                if (blob != null) {
                    try {
                        AddThumbnailUnrestricted runner = new AddThumbnailUnrestricted(
                                context.getCoreSession(), doc, blobHolder);
                        runner.run();
                        context.getCoreSession().save();
                        return;
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            // No Blob anymore, remove the facet
            if (doc.hasFacet(ThumbnailConstants.THUMBNAIL_FACET)) {
                doc.removeFacet(ThumbnailConstants.THUMBNAIL_FACET);
                CoreSession coreSession = context.getCoreSession();
                coreSession.saveDocument(doc);
                coreSession.save();
            }
        }
    }

    @Override
    public void handleEvent(EventBundle events) throws ClientException {
        if (!events.containsEventName(DocumentEventTypes.DOCUMENT_CREATED)
                && !events.containsEventName(DocumentEventTypes.DOCUMENT_UPDATED)) {
            return;
        }
        for (Event event : events) {
            if (DocumentEventTypes.DOCUMENT_CREATED.equals(event.getName())
                    || DocumentEventTypes.DOCUMENT_UPDATED.equals(event.getName())) {
                handleEvent(event);
            }
        }

    }

}