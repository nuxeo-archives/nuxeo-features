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
 *
 */
package org.nuxeo.ecm.platform.picture.thumbnail;

import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.thumbnail.ThumbnailFactory;
import org.nuxeo.ecm.platform.picture.api.PictureView;
import org.nuxeo.ecm.platform.picture.api.adapters.MultiviewPicture;

/**
 * Picture thumbnail factory
 * 
 * @since 5.7
 */
public class ThumbnailPictureFactory implements ThumbnailFactory {

    @Override
    public Blob getThumbnail(DocumentModel doc) throws ClientException {
        if (!doc.hasFacet("Picture")) {
            throw new ClientException("Document is not a picture");
        }
        MultiviewPicture mViewPicture = doc.getAdapter(MultiviewPicture.class);
        PictureView thumbnailView = mViewPicture.getView("Thumbnail");
        if (thumbnailView != null) {
            thumbnailView.getBlob();
        }
        // TODO: thumbnail converter in case there is no nuxeo picture default
        // thumbnail
        return null;
    }
}
