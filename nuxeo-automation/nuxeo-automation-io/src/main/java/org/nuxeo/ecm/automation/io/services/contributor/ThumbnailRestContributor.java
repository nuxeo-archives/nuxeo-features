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
 *     Vladimir Pasquier <vpasquier@nuxeo.com>
 */
package org.nuxeo.ecm.automation.io.services.contributor;

import org.codehaus.jackson.JsonGenerator;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.thumbnail.ThumbnailAdapter;
import org.nuxeo.ecm.platform.ui.web.tag.fn.DocumentModelFunctions;

import java.io.IOException;

/**
 * This contributor adds a document Thumbnail Download URL
 *
 * @since 5.9.5
 */
public class ThumbnailRestContributor implements RestContributor {

    @Override
    public void contribute(JsonGenerator jg, RestEvaluationContext ec)
            throws ClientException, IOException {
        DocumentModel doc = ec.getDocumentModel();
        ThumbnailAdapter thumbnailAdapter = doc.getAdapter(ThumbnailAdapter
                .class);
        jg.writeStartObject();
        if (thumbnailAdapter != null) {
            try {
                Blob thumbnail = thumbnailAdapter.getThumbnail(doc
                        .getCoreSession());
                if (thumbnail != null) {
                    String url = DocumentModelFunctions.fileUrl("downloadFile",
                            doc, "thumb:thumbnail", thumbnail.getFilename());
                    jg.writeStringField("thumbnailUrl", url);
                } else {
                    writeEmptyThumbnail(jg);
                }
            } catch (ClientException e) {
                writeEmptyThumbnail(jg);
            }
        } else {
            writeEmptyThumbnail(jg);
        }
        jg.writeEndObject();
        jg.flush();
    }

    private void writeEmptyThumbnail(JsonGenerator jg) throws IOException {
        jg.writeStringField("thumbnailUrl", null);
    }

}
