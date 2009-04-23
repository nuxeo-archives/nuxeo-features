package org.nuxeo.webengine.sites.fragments;

import java.util.List;
import java.util.Map;

import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.webengine.WebEngine;
import org.nuxeo.ecm.webengine.model.WebContext;
import org.nuxeo.theme.fragments.AbstractFragment;
import org.nuxeo.theme.models.Model;
import org.nuxeo.theme.models.ModelException;
import org.nuxeo.webengine.sites.models.ContextualLinkListModel;
import org.nuxeo.webengine.sites.models.ContextualLinkModel;
import org.nuxeo.webengine.utils.SiteUtils;

public class ContextualLinkFragment extends AbstractFragment {

    @Override
    public Model getModel() throws ModelException {
        ContextualLinkListModel model = new ContextualLinkListModel();
        WebContext ctx = WebEngine.getActiveContext();
        CoreSession session = ctx.getCoreSession();
        DocumentModel doc = (DocumentModel) ctx.getTargetObject();
        List<Object> objects = null;
        try {
            objects = SiteUtils.getContextualLinks(session, doc);
        } catch (Exception e) {
            throw new ModelException(e);
        }
        for (Object linkObject : objects) {
            Map<String, String> mapLink = (Map<String, String>)linkObject;
            ContextualLinkModel linkModel = new ContextualLinkModel(
                    mapLink.get("title"), mapLink.get("description"), mapLink.get("link"));
            model.addItem(linkModel);
        }

        return model;
    }

}
