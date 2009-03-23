package org.nuxeo.ecm.platform.publishing;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.platform.ec.notification.service.NotificationServiceHelper;
import org.nuxeo.ecm.platform.url.DocumentLocationImpl;
import org.nuxeo.ecm.platform.url.DocumentViewImpl;
import org.nuxeo.ecm.platform.url.api.DocumentLocation;
import org.nuxeo.ecm.platform.url.api.DocumentView;
import org.nuxeo.ecm.platform.url.api.DocumentViewCodecManager;
import org.nuxeo.runtime.api.Framework;

public final class PublishHelper {

    private static final Log log = LogFactory.getLog(PublishingActionsListenerBean.class);

    private static DocumentViewCodecManager docLocator;

    public static String getUrlFromDocument(DocumentModel doc) {
        DocumentLocation docLoc = new DocumentLocationImpl(
                doc.getRepositoryName(), doc.getRef());
        DocumentView docView = new DocumentViewImpl(docLoc);
        docView.setViewId("view_documents");
        return getDocLocator().getUrlFromDocumentView(
                docView,
                true,
                NotificationServiceHelper.getNotificationService().getServerUrlPrefix());
    }

    private static DocumentViewCodecManager getDocLocator() {
        if (docLocator == null) {
            try {
                docLocator = Framework.getService(DocumentViewCodecManager.class);
            } catch (Exception e) {
                log.info("Could not get service for document view manager");
            }
        }

        return docLocator;
    }
}
