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
 * 
 */
package org.nuxeo.ecm.platform.thumbnail;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.UnrestrictedSessionRunner;
import org.nuxeo.ecm.core.api.blobholder.BlobHolder;
import org.nuxeo.ecm.core.api.thumbnail.Thumbnail;
import org.nuxeo.ecm.core.convert.api.ConversionService;
import org.nuxeo.runtime.api.Framework;

/**
 * Thumbnail bean in session unrestricted to add/update thumbnail facet to a
 * document and store doc thumbnail
 * 
 * @since 5.7
 */
public class AddThumbnailUnrestricted extends UnrestrictedSessionRunner {

    private static final Log log = LogFactory.getLog(AddThumbnailUnrestricted.class);

    protected ConversionService conversionService;

    protected DocumentModel doc;

    protected BlobHolder blobHolder;

    protected Thumbnail thumbnail = null;

    public AddThumbnailUnrestricted(CoreSession coreSession, DocumentModel doc,
            BlobHolder blobHolder) {
        super(coreSession);
        this.doc = doc;
        this.blobHolder = blobHolder;
    }

    @Override
    public void run() throws ClientException {
        try {
            Blob thumbnailBlob = null;
            try {
                conversionService = Framework.getService(ConversionService.class);
                Map<String, Serializable> params = new HashMap<String, Serializable>();
                // TODO: convert non pix attachment
                // Image converter before thumbnail converter
                // params.put("targetFilePath", "readyToThumbnail.png");
                // BlobHolder bh = conversionService.convertToMimeType(
                // ThumbnailConstants.THUMBNAIL_MIME_TYPE, blobHolder,
                // params);
                // params.clear();
                // Thumbnail converter
                params.put(ThumbnailConstants.THUMBNAIL_SIZE_PARAMETER_NAME,
                        ThumbnailConstants.THUMBNAIL_DEFAULT_SIZE);
                BlobHolder bh = conversionService.convert(
                        ThumbnailConstants.THUMBNAIL_CONVERTER_NAME,
                        blobHolder, params);
                if (bh != null) {
                    thumbnailBlob = bh.getBlob();
                }
            } catch (ClientException e) {
                log.debug("Unable to convert document blob in thumbnail", e);
            } finally {
                if (thumbnailBlob != null) {
                    doc.setPropertyValue(
                            ThumbnailConstants.THUMBNAIL_PROPERTY_NAME,
                            (Serializable) thumbnailBlob);
                    session.save();
                }
            }
        } catch (Exception e) {
            log.warn("Error while adding thumbnail", e);
        }
    }
}
