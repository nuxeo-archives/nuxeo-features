/*
 * (C) Copyright 2013 Nuxeo SA (http://nuxeo.com/) and contributors.
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
 *     guillaume
 */
package org.nuxeo.ecm.platform.tag.web;

import static org.jboss.seam.ScopeType.EVENT;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.contexts.Contexts;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessage;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentRef;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.platform.tag.Tag;
import org.nuxeo.ecm.platform.tag.TagService;
import org.nuxeo.ecm.platform.ui.select2.common.Select2Common;
import org.nuxeo.ecm.platform.ui.web.api.NavigationContext;
import org.nuxeo.runtime.api.Framework;

/**
 *
 *
 * @since TODO
 */
@Name("tagSelect2Support")
@Scope(EVENT)
public class TagSelect2Support {

    @In(create = true)
    protected NavigationContext navigationContext;

    @In(create = true, required = false)
    protected transient CoreSession documentManager;

    @In(create = true, required = false)
    protected transient FacesMessages facesMessages;

    @In(create = true)
    protected Map<String, String> messages;

    protected String label;

    @Factory(value = "resolveDocumentTags", scope = EVENT)
    public String resolveDocumentTags() throws ClientException {
        final List<Tag> tagList = (List<Tag>) Contexts.getEventContext().get(
                "currentDocumentTags");
        if (tagList == null || tagList.isEmpty()) {
            return "";
        } else {
            JSONArray result = new JSONArray();
            for (Tag tag : tagList) {
                JSONObject obj = new JSONObject();
                obj.element(Select2Common.ID, tag.getLabel());
                obj.element(Select2Common.LABEL, tag.getLabel());
                result.add(obj);
            }
            return result.toString();
        }
    }

    @Factory(value = "documentTagIds", scope = EVENT)
    public List<String> getDocumentTagStrings() throws ClientException {
        final List<Tag> tagList = (List<Tag>) Contexts.getEventContext().get(
                "currentDocumentTags");
        if (tagList == null || tagList.isEmpty()) {
            return null;
        } else {
            List<String> result = new ArrayList<String>();
            for (Tag tag : tagList) {
                result.add(tag.getLabel());
            }
            return result;
        }
    }

    /**
     * Performs the tagging on the current document.
     */
    public String addTagging() throws ClientException {
        String messageKey;
        if (StringUtils.isBlank(label)) {
            messageKey = "message.add.new.tagging.not.empty";
        } else {
            DocumentModel currentDocument = navigationContext.getCurrentDocument();
            String docId = currentDocument.getId();

            TagService tagService = getTagService();
            tagService.tag(documentManager, docId, label, null);
            if (currentDocument.isVersion()) {
                DocumentModel liveDocument = documentManager.getSourceDocument(currentDocument.getRef());
                if (!liveDocument.isCheckedOut()) {
                    tagService.tag(documentManager, liveDocument.getId(),
                            label, null);
                }
            } else if (!currentDocument.isCheckedOut()) {
                DocumentRef ref = documentManager.getBaseVersion(currentDocument.getRef());
                if (ref instanceof IdRef) {
                    tagService.tag(documentManager, ref.toString(), label, null);
                }
            }
            messageKey = "message.add.new.tagging";
            // force invalidation
            Contexts.getEventContext().remove("resolveDocumentTags");
        }
        facesMessages.add(StatusMessage.Severity.INFO,
                messages.get(messageKey), label);
        reset();
        return null;
    }

    public String removeTagging() throws ClientException {
        DocumentModel currentDocument = navigationContext.getCurrentDocument();
        String docId = currentDocument.getId();

        TagService tagService = getTagService();
        tagService.untag(documentManager, docId, label, null);

        if (currentDocument.isVersion()) {
            DocumentModel liveDocument = documentManager.getSourceDocument(currentDocument.getRef());
            if (!liveDocument.isCheckedOut()) {
                tagService.untag(documentManager, liveDocument.getId(), label,
                        null);
            }
        } else if (!currentDocument.isCheckedOut()) {
            DocumentRef ref = documentManager.getBaseVersion(currentDocument.getRef());
            if (ref instanceof IdRef) {
                tagService.untag(documentManager, ref.toString(), label, null);
            }
        }
        // force invalidation
        Contexts.getEventContext().remove("currentDocumentTags");
        facesMessages.add(StatusMessage.Severity.INFO,
                messages.get("message.remove.tagging"),
                label);
        reset();
        return null;
    }

    protected void reset() {
        label = null;
    }

    protected TagService getTagService() throws ClientException {
        TagService tagService;
        try {
            tagService = Framework.getService(TagService.class);
        } catch (Exception e) {
            throw new ClientException(e);
        }
        if (tagService == null) {
            return null;
        }
        return tagService.isEnabled() ? tagService : null;
    }

    public String encodePrameters() {
        JSONObject obj = new JSONObject();
        obj.put("multiple", "true");
        obj.put("minChars", "1");
        obj.put("readonly", "false");
        obj.put("createSearchChoice", "createNewTag");
        obj.put("operationId", "Tag.Suggestion");
        obj.put("width", "100%");
        obj.put("placeholder", "Add a tag");
        obj.put("onAddEntryHandler", "addTagHandler");
        obj.put("onRemoveEntryHandler", "removeTagHandler");
        obj.put("containerCssClass", "s2tagContainerCssClass");
        obj.put("dropdownCssClass", "s2tagDropdownCssClass");
        JSONArray tokenSeparator = new JSONArray();
        tokenSeparator.add(",");
        tokenSeparator.add(" ");
        obj.put("tokenSeparators", tokenSeparator);
        return obj.toString();
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
