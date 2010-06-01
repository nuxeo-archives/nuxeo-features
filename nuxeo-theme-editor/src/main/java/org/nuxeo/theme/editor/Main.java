package org.nuxeo.theme.editor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.nuxeo.ecm.webengine.WebEngine;
import org.nuxeo.ecm.webengine.forms.FormData;
import org.nuxeo.ecm.webengine.model.Access;
import org.nuxeo.ecm.webengine.model.WebObject;
import org.nuxeo.ecm.webengine.model.impl.ModuleRoot;
import org.nuxeo.theme.Manager;
import org.nuxeo.theme.elements.Element;
import org.nuxeo.theme.elements.ElementFormatter;
import org.nuxeo.theme.elements.PageElement;
import org.nuxeo.theme.elements.ThemeElement;
import org.nuxeo.theme.formats.Format;
import org.nuxeo.theme.formats.FormatType;
import org.nuxeo.theme.formats.layouts.Layout;
import org.nuxeo.theme.formats.styles.Style;
import org.nuxeo.theme.formats.widgets.Widget;
import org.nuxeo.theme.fragments.Fragment;
import org.nuxeo.theme.fragments.FragmentType;
import org.nuxeo.theme.perspectives.PerspectiveManager;
import org.nuxeo.theme.perspectives.PerspectiveType;
import org.nuxeo.theme.presets.PresetManager;
import org.nuxeo.theme.presets.PresetType;
import org.nuxeo.theme.resources.ResourceBank;
import org.nuxeo.theme.themes.ThemeDescriptor;
import org.nuxeo.theme.themes.ThemeException;
import org.nuxeo.theme.themes.ThemeIOException;
import org.nuxeo.theme.themes.ThemeManager;
import org.nuxeo.theme.themes.ThemeSerializer;
import org.nuxeo.theme.types.Type;
import org.nuxeo.theme.types.TypeFamily;
import org.nuxeo.theme.uids.Identifiable;
import org.nuxeo.theme.views.ViewType;

import edu.emory.mathcs.backport.java.util.Arrays;

@WebObject(type = "nxthemes-editor", administrator = Access.GRANT)
@Produces(MediaType.TEXT_HTML)
public class Main extends ModuleRoot {

    @GET
    @Path("perspectiveSelector")
    public Object renderPerspectiveSelector(
            @QueryParam("org.nuxeo.theme.application.path") String path,
            @QueryParam("org.nuxeo.theme.application.name") String name) {
        return getTemplate("perspectiveSelector.ftl").arg("perspectives",
                getPerspectives());
    }

    @GET
    @Path("themeSelector")
    public Object renderThemeSelector(
            @QueryParam("org.nuxeo.theme.application.path") String path,
            @QueryParam("org.nuxeo.theme.application.name") String name) {
        return getTemplate("themeSelector.ftl").arg("themes",
                getWorkspaceThemes(path, name));
    }

    @GET
    @Path("pageSelector")
    public Object renderPageSelector(
            @QueryParam("org.nuxeo.theme.application.path") String path,
            @QueryParam("org.nuxeo.theme.application.name") String name) {
        return getTemplate("pageSelector.ftl").arg("current_theme_name",
                getCurrentThemeName(path, name)).arg("pages",
                getPages(path, name));
    }

    @GET
    @Path("canvasModeSelector")
    public Object renderCanvasModeSelector(
            @QueryParam("org.nuxeo.theme.application.path") String path,
            @QueryParam("org.nuxeo.theme.application.name") String name) {
        return getTemplate("canvasModeSelector.ftl");
    }

    @GET
    @Path("presetManager")
    public Object renderPresetManager(
            @QueryParam("org.nuxeo.theme.application.path") String path,
            @QueryParam("org.nuxeo.theme.application.name") String name) {
        return getTemplate("presetManager.ftl").arg("current_theme_name",
                getCurrentThemeName(path, name)).arg("preset_manager_mode",
                getPresetManagerMode()).arg("selected_preset_category",
                getSelectedPresetCategory()).arg("preset_groups",
                getPresetGroups(null)).arg("selected_preset_group",
                getSelectedPresetGroup());
    }

    @GET
    @Path("styleManager")
    public Object renderStyleManager(
            @QueryParam("org.nuxeo.theme.application.path") String path,
            @QueryParam("org.nuxeo.theme.application.name") String name) {
        ThemeManager themeManager = Manager.getThemeManager();
        List<Style> styles = getNamedStyles(path, name);

        Style selectedStyle = getSelectedNamedStyle();
        if (!styles.contains(selectedStyle) && !styles.isEmpty()) {
            selectedStyle = styles.get(0);
        }
        List<Style> rootStyles = new ArrayList<Style>();
        for (Style style : styles) {
            if (ThemeManager.getAncestorFormatOf(style) == null) {
                rootStyles.add(style);
            }
        }

        String currentThemeName = getCurrentThemeName(path, name);
        String templateEngine = getTemplateEngine(path);
        ThemeDescriptor currentThemeDef = themeManager.getThemeDescriptorByThemeName(
                templateEngine, currentThemeName);
        return getTemplate("styleManager.ftl").arg("theme", currentThemeDef).arg(
                "named_styles", styles).arg("style_manager_mode",
                getStyleManagerMode()).arg("selected_named_style",
                selectedStyle).arg("selected_named_style_css",
                getRenderedPropertiesForNamedStyle(selectedStyle)).arg(
                "current_theme_name", currentThemeName).arg("page_styles",
                getPageStyles(currentThemeName)).arg("root_styles", rootStyles);
    }

    @GET
    @Path("themeBrowser")
    public Object renderThemeBrowser(
            @QueryParam("org.nuxeo.theme.application.path") String path,
            @QueryParam("org.nuxeo.theme.application.name") String name) {
        List<String> availableThemes = new ArrayList<String>();
        List<ThemeInfo> workspaceThemes = getWorkspaceThemes(path, name);
        List<String> workspaceThemeNames = new ArrayList<String>();
        for (ThemeInfo theme : workspaceThemes) {
            workspaceThemeNames.add(theme.getName());
        }
        for (ThemeDescriptor themeDef : ThemeManager.getThemeDescriptors()) {
            if (!workspaceThemeNames.contains(themeDef.getName())) {
                availableThemes.add(themeDef.getName());
            }
        }
        return getTemplate("themeBrowser.ftl").arg("available_themes",
                availableThemes).arg("workspace_themes", workspaceThemes);
    }

    @GET
    @Path("backToCanvas")
    public Object renderBackToCanvas(
            @QueryParam("org.nuxeo.theme.application.path") String path,
            @QueryParam("org.nuxeo.theme.application.name") String name) {
        return getTemplate("backToCanvas.ftl");
    }

    @GET
    @Path("themeActions")
    public Object renderThemeActions(
            @QueryParam("org.nuxeo.theme.application.path") String path,
            @QueryParam("org.nuxeo.theme.application.name") String name) {
        ThemeManager themeManager = Manager.getThemeManager();
        String currentThemeName = getCurrentThemeName(path, name);
        String templateEngine = getTemplateEngine(path);
        String currentPagePath = getCurrentPagePath(path, name);
        String currentpageName = ThemeManager.getPageNameFromPagePath(currentPagePath);
        ThemeDescriptor currentThemeDef = ThemeManager.getThemeDescriptorByThemeName(
                templateEngine, currentThemeName);
        return getTemplate("themeActions.ftl").arg("theme", currentThemeDef).arg(
                "current_page_path", currentPagePath).arg("current_page_name",
                currentpageName);
    }

    @GET
    @Path("presetManagerActions")
    public Object renderPresetManagerActions(
            @QueryParam("org.nuxeo.theme.application.path") String path,
            @QueryParam("org.nuxeo.theme.application.name") String name) {
        ThemeManager themeManager = Manager.getThemeManager();
        String currentThemeName = getCurrentThemeName(path, name);
        String templateEngine = getTemplateEngine(path);
        ThemeDescriptor currentThemeDef = themeManager.getThemeDescriptorByThemeName(
                templateEngine, currentThemeName);
        return getTemplate("presetManagerActions.ftl").arg("theme",
                currentThemeDef).arg("selected_preset_category",
                getSelectedPresetCategory());
    }

    @GET
    @Path("styleManagerActions")
    public Object renderStyleManagerActions(
            @QueryParam("org.nuxeo.theme.application.path") String path,
            @QueryParam("org.nuxeo.theme.application.name") String name) {
        ThemeManager themeManager = Manager.getThemeManager();
        String currentThemeName = getCurrentThemeName(path, name);
        String templateEngine = getTemplateEngine(path);
        ThemeDescriptor currentThemeDef = themeManager.getThemeDescriptorByThemeName(
                templateEngine, currentThemeName);
        return getTemplate("styleManagerActions.ftl").arg("theme",
                currentThemeDef);
    }

    @GET
    @Path("viewModes")
    public Object renderViewModes(
            @QueryParam("org.nuxeo.theme.application.path") String path,
            @QueryParam("org.nuxeo.theme.application.name") String name) {
        return getTemplate("viewModes.ftl");
    }

    @GET
    @Path("themeBrowserActions")
    public Object renderThemeBrowserActions(
            @QueryParam("org.nuxeo.theme.application.path") String path,
            @QueryParam("org.nuxeo.theme.application.name") String name) {
        return getTemplate("themeBrowserActions.ftl");
    }

    @GET
    @Path("undoActions")
    public Object renderUndoActions(
            @QueryParam("org.nuxeo.theme.application.path") String path,
            @QueryParam("org.nuxeo.theme.application.name") String name) {
        String themeName = getCurrentThemeName(path, name);
        UndoBuffer undoBuffer = SessionManager.getUndoBuffer(themeName);
        return getTemplate("undoActions.ftl").arg("current_theme_name",
                themeName).arg("undo_buffer", undoBuffer);
    }

    @GET
    @Path("fragmentFactory")
    public Object renderFragmentFactory(
            @QueryParam("org.nuxeo.theme.application.path") String path,
            @QueryParam("org.nuxeo.theme.application.name") String name) {
        String fragmentType = getSelectedFragmentType();
        String fragmentView = getSelectedFragmentView();
        String fragmentStyle = getSelectedFragmentStyle();
        return getTemplate("fragmentFactory.ftl").arg("current_theme_name",
                getCurrentThemeName(path, name)).arg("selected_fragment_type",
                fragmentType).arg("selected_fragment_view", fragmentView).arg(
                "selected_fragment_style", fragmentStyle).arg("fragments",
                getFragments(path)).arg("styles", getNamedStyles(path, name)).arg(
                "views", getViews(fragmentType, path)).arg(
                "selected_element_id", getSelectedElementId());
    }

    @GET
    @Path("elementEditor")
    public Object renderElementEditor(
            @QueryParam("org.nuxeo.theme.application.path") String path,
            @QueryParam("org.nuxeo.theme.application.name") String name) {
        return getTemplate("elementEditor.ftl").arg("selected_element",
                getSelectedElement());
    }

    @GET
    @Path("elementDescription")
    public Object renderElementDescription(
            @QueryParam("org.nuxeo.theme.application.path") String path,
            @QueryParam("org.nuxeo.theme.application.name") String name) {
        return getTemplate("elementDescription.ftl").arg("selected_element",
                getSelectedElement());
    }

    @GET
    @Path("elementPadding")
    public Object renderElementPadding(
            @QueryParam("org.nuxeo.theme.application.path") String path,
            @QueryParam("org.nuxeo.theme.application.name") String name) {
        return getTemplate("elementPadding.ftl").arg("selected_element",
                getSelectedElement()).arg("padding_of_selected_element",
                getPaddingOfSelectedElement());
    }

    @GET
    @Path("elementProperties")
    public Object renderElementProperties(
            @QueryParam("org.nuxeo.theme.application.path") String path,
            @QueryParam("org.nuxeo.theme.application.name") String name) {
        return getTemplate("elementProperties.ftl").arg("selected_element",
                getSelectedElement()).arg("element_properties",
                getSelectedElementProperties());
    }

    @GET
    @Path("elementStyle")
    public Object renderElementStyle(
            @QueryParam("org.nuxeo.theme.application.path") String path,
            @QueryParam("org.nuxeo.theme.application.name") String name) {
        return getTemplate("elementStyle.ftl").arg("selected_element",
                getSelectedElement()).arg("style_of_selected_element",
                getStyleOfSelectedElement()).arg("current_theme_name",
                getCurrentThemeName(path, name)).arg(
                "style_layers_of_selected_element",
                getStyleLayersOfSelectedElement()).arg(
                "inherited_style_name_of_selected_element",
                getInheritedStyleNameOfSelectedElement()).arg("named_styles",
                getNamedStyles(path, name));
    }

    @GET
    @Path("elementWidget")
    public Object renderElementWidget(
            @QueryParam("org.nuxeo.theme.application.path") String path,
            @QueryParam("org.nuxeo.theme.application.name") String name) {
        return getTemplate("elementWidget.ftl").arg("selected_element",
                getSelectedElement()).arg("selected_view_name",
                getViewNameOfSelectedElement()).arg(
                "view_names_for_selected_element",
                getViewNamesForSelectedElement(path));
    }

    @GET
    @Path("elementVisibility")
    public Object renderElementVisibility(
            @QueryParam("org.nuxeo.theme.application.path") String path,
            @QueryParam("org.nuxeo.theme.application.name") String name) {
        return getTemplate("elementVisibility.ftl").arg("selected_element",
                getSelectedElement()).arg("perspectives_of_selected_element",
                getPerspectivesOfSelectedElement()).arg(
                "is_selected_element_always_visible",
                isSelectedElementAlwaysVisible()).arg("perspectives",
                getPerspectives());
    }

    @GET
    @Path("stylePicker")
    public Object renderStylePicker(
            @QueryParam("org.nuxeo.theme.application.path") String path,
            @QueryParam("org.nuxeo.theme.application.name") String name) {
        return getTemplate("stylePicker.ftl").arg("style_category",
                getSelectedStyleCategory()).arg("current_theme_name",
                getCurrentThemeName(path, name)).arg("selected_preset_group",
                getSelectedPresetGroup()).arg("preset_groups",
                getPresetGroupsForSelectedCategory()).arg(
                "presets_for_selected_group",
                getPresetsForSelectedGroup(path, name));
    }

    @GET
    @Path("areaStyleChooser")
    public Object renderAreaStyleChooser(
            @QueryParam("org.nuxeo.theme.application.path") String path,
            @QueryParam("org.nuxeo.theme.application.name") String name) {
        return getTemplate("areaStyleChooser.ftl").arg("style_category",
                getSelectedStyleCategory()).arg("current_theme_name",
                getCurrentThemeName(path, name)).arg("preset_groups",
                getPresetGroupsForSelectedCategory()).arg(
                "presets_for_selected_group",
                getPresetsForSelectedGroup(path, name)).arg(
                "selected_preset_group", getSelectedPresetGroup());
    }

    @GET
    @Path("styleProperties")
    public Object renderStyleProperties(
            @QueryParam("org.nuxeo.theme.application.path") String path,
            @QueryParam("org.nuxeo.theme.application.name") String name) {
        return getTemplate("styleProperties.ftl").arg("selected_element",
                getSelectedElement()).arg("style_edit_mode", getStyleEditMode()).arg(
                "style_selectors", getStyleSelectorsForSelectedElement()).arg(
                "rendered_style_properties",
                getRenderedStylePropertiesForSelectedElement()).arg(
                "selected_style_selector", getSelectedStyleSelector()).arg(
                "element_style_properties",
                getStylePropertiesForSelectedElement()).arg(
                "all_style_properties",
                getAvailableStylePropertiesForSelectedElement()).arg(
                "selected_view_name", getViewNameOfSelectedElement()).arg(
                "selected_css_categories", getSelectedCssCategories());
    }

    @GET
    @Path("render_css_preview")
    public String renderCssPreview() {
        String selectedElementId = getSelectedElementId();
        Style selectedStyleLayer = getSelectedStyleLayer();
        String selectedViewName = getViewNameOfSelectedElement();
        Element selectedElement = getSelectedElement();
        return Editor.renderCssPreview(selectedElement, selectedStyleLayer,
                selectedViewName);
    }

    @GET
    @Path("skinManager")
    public Object renderSkinManager(
            @QueryParam("org.nuxeo.theme.application.path") String path,
            @QueryParam("org.nuxeo.theme.application.name") String name) {
        String bankName = getSelectedBankName();
        return getTemplate("skinManager.ftl").arg("selected_bank_name",
                bankName).arg("skins", getBankSkins(bankName)).arg("banks",
                ThemeManager.getResourceBanks());
    }

    public static String getSelectedBankName() {
        return SessionManager.getResourceBank();
    }

    public static List<Map<String, String>> getBankSkins(String bankName) {
        if (bankName != null) {
            ResourceBank resourceBank = ThemeManager.getResourceBank(bankName);
            return resourceBank.getSkins();
        }
        return null;
    }

    @POST
    @Path("select_resource_bank")
    public void selectResourceBank() {
        FormData form = ctx.getForm();
        String name = form.getString("name");
        SessionManager.setResourceBank(name);
    }

    @GET
    @Path("xml_export")
    public Response xmlExport(@QueryParam("src") String src,
            @QueryParam("download") Integer download,
            @QueryParam("indent") Integer indent) {
        if (src == null) {
            return null;
        }
        Manager.getThemeManager();
        ThemeDescriptor themeDef;
        try {
            themeDef = ThemeManager.getThemeDescriptor(src);
        } catch (ThemeException e) {
            throw new ThemeEditorException(e.getMessage(), e);
        }

        ThemeSerializer serializer = new ThemeSerializer();
        if (indent == null) {
            indent = 0;
        }

        String xml;
        try {
            xml = serializer.serializeToXml(src, indent);
        } catch (ThemeIOException e) {
            throw new ThemeEditorException(e.getMessage(), e);
        }

        ResponseBuilder builder = Response.ok(xml);
        if (download != null) {
            builder.header("Content-disposition", String.format(
                    "attachment; filename=theme-%s.xml", themeDef.getName()));
        }
        builder.type("text/xml");
        return builder.build();
    }

    @POST
    @Path("clear_selections")
    public void clearSelections() {
        SessionManager.setElementId(null);
        SessionManager.setStyleEditMode(null);
        SessionManager.setStyleLayerId(null);
        SessionManager.setStyleSelector(null);
        SessionManager.setNamedStyleId(null);
        SessionManager.setStyleCategory(null);
        SessionManager.setPresetGroup(null);
        SessionManager.setPresetCategory(null);
        SessionManager.setClipboardElementId(null);
        SessionManager.setFragmentType(null);
        SessionManager.setFragmentView(null);
        SessionManager.setFragmentStyle(null);
    }

    @POST
    @Path("select_element")
    public void selectElement() {
        String id = ctx.getForm().getString("id");
        SessionManager.setElementId(id);
        // clean up
        SessionManager.setStyleEditMode(null);
        SessionManager.setStyleLayerId(null);
        SessionManager.setNamedStyleId(null);
        SessionManager.setStyleSelector(null);
        SessionManager.setStyleCategory(null);
        SessionManager.setPresetGroup(null);
        SessionManager.setPresetCategory(null);
        SessionManager.setFragmentType(null);
        SessionManager.setFragmentView(null);
        SessionManager.setFragmentStyle(null);
    }

    @POST
    @Path("add_page")
    public String addPage() {
        String pagePath = ctx.getForm().getString("path");
        try {
            return Editor.addPage(pagePath);
        } catch (Exception e) {
            throw new ThemeEditorException(e.getMessage(), e);
        }
    }

    @POST
    @Path("add_theme")
    public String addTheme() {
        String name = ctx.getForm().getString("name");
        try {
            return Editor.addTheme(name);
        } catch (Exception e) {
            throw new ThemeEditorException(e.getMessage(), e);
        }
    }

    @POST
    @Path("align_element")
    public void alignElement() {
        FormData form = ctx.getForm();
        String id = form.getString("id");
        String position = form.getString("position");
        Element element = ThemeManager.getElementById(id);
        try {
            Editor.alignElement(element, position);
        } catch (Exception e) {
            throw new ThemeEditorException(e.getMessage(), e);
        }

    }

    @POST
    @Path("assign_style_property")
    public void assignStyleProperty() {
        FormData form = ctx.getForm();
        String id = form.getString("element_id");
        String propertyName = form.getString("property");
        String value = form.getString("value");
        Element element = ThemeManager.getElementById(id);
        try {
            Editor.assignStyleProperty(element, propertyName, value);
        } catch (Exception e) {
            throw new ThemeEditorException(e.getMessage(), e);
        }
    }

    @POST
    @Path("copy_element")
    public void copyElement() {
        String id = ctx.getForm().getString("id");
        SessionManager.setClipboardElementId(id);
    }

    @POST
    @Path("set_preset_category")
    public void setPresetCategory() {
        String themeName = ctx.getForm().getString("theme_name");
        String presetName = ctx.getForm().getString("preset_name");
        String category = ctx.getForm().getString("category");
        try {
            Editor.setPresetCategory(themeName, presetName, category);
        } catch (Exception e) {
            throw new ThemeEditorException(e.getMessage(), e);
        }
    }

    @POST
    @Path("copy_preset")
    public void copyPreset() {
        String id = ctx.getForm().getString("id");
        SessionManager.setClipboardPresetId(id);
    }

    @POST
    @Path("paste_preset")
    public void pastePreset() {
        FormData form = ctx.getForm();
        String themeName = form.getString("theme_name");
        String newPresetName = form.getString("preset_name");
        String presetName = getClipboardPreset();
        if (presetName == null) {
            throw new ThemeEditorException("Nothing to paste");
        }
        PresetType preset = PresetManager.getPresetByName(presetName);
        if (preset == null) {
            throw new ThemeEditorException("Preset not found: " + presetName);
        }
        try {
            Editor.addPreset(themeName, newPresetName, preset.getCategory(),
                    preset.getValue());
        } catch (Exception e) {
            throw new ThemeEditorException(e.getMessage(), e);
        }
    }

    @POST
    @Path("create_named_style")
    public void createNamedStyle() {
        FormData form = ctx.getForm();
        String id = form.getString("id");
        String themeName = form.getString("theme_name");
        String styleName = form.getString("style_name");
        Element element = null;
        if (id == null) {
            element = ThemeManager.getElementById(id);
        }
        try {
            Editor.createNamedStyle(element, styleName, themeName);
        } catch (Exception e) {
            throw new ThemeEditorException(e.getMessage(), e);
        }
    }

    @POST
    @Path("create_style")
    public void createStyle() {
        Element element = getSelectedElement();
        try {
            Editor.createStyle(element);
        } catch (Exception e) {
            throw new ThemeEditorException(e.getMessage(), e);
        }
    }

    @POST
    @Path("delete_element")
    public void deleteElement() {
        FormData form = ctx.getForm();
        String id = form.getString("id");
        Element element = ThemeManager.getElementById(id);
        try {
            Editor.deleteElement(element);
        } catch (Exception e) {
            throw new ThemeEditorException(e.getMessage(), e);
        }
    }

    @POST
    @Path("delete_named_style")
    public void deleteNamedStyle() {
        FormData form = ctx.getForm();
        String id = form.getString("id");
        String themeName = form.getString("theme_name");
        String styleName = form.getString("style_name");
        Element element = ThemeManager.getElementById(id);
        try {
            Editor.deleteNamedStyle(element, styleName, themeName);
        } catch (Exception e) {
            throw new ThemeEditorException(e.getMessage(), e);
        }
    }

    @POST
    @Path("duplicate_element")
    public String duplicateElement() {
        FormData form = ctx.getForm();
        String id = form.getString("id");
        Element element = ThemeManager.getElementById(id);
        try {
            Integer res = Editor.duplicateElement(element);
            return String.valueOf(res);
        } catch (Exception e) {
            throw new ThemeEditorException(e.getMessage(), e);
        }
    }

    @POST
    @Path("insert_fragment")
    public void insertFragment() {
        FormData form = ctx.getForm();
        String destId = form.getString("dest_id");
        String typeName = form.getString("type_name");
        String styleName = form.getString("style_name");
        Element destElement = ThemeManager.getElementById(destId);
        try {
            Editor.insertFragment(destElement, typeName, styleName);
        } catch (Exception e) {
            throw new ThemeEditorException(e.getMessage(), e);
        }
    }

    @POST
    @Path("insert_section_after")
    public void insertSectionAfter() {
        FormData form = ctx.getForm();
        String id = form.getString("id");
        Element element = ThemeManager.getElementById(id);
        try {
            Editor.insertSectionAfter(element);
        } catch (Exception e) {
            throw new ThemeEditorException(e.getMessage(), e);
        }
    }

    @POST
    @Path("select_preset_manager_mode")
    public void selectPresetManagerMode() {
        FormData form = ctx.getForm();
        String mode = form.getString("mode");
        SessionManager.setPresetManagerMode(mode);
    }

    @POST
    @Path("select_fragment_type")
    public void selectFragmentType() {
        FormData form = ctx.getForm();
        String type = form.getString("type");
        SessionManager.setFragmentType(type);
        SessionManager.setFragmentView(null);
        SessionManager.setFragmentStyle(null);
    }

    @POST
    @Path("select_fragment_view")
    public void selectFragmentView() {
        FormData form = ctx.getForm();
        String view = form.getString("view");
        SessionManager.setFragmentView(view);
        SessionManager.setFragmentStyle(null);
    }

    @POST
    @Path("select_fragment_style")
    public void selectFragmentStyle() {
        FormData form = ctx.getForm();
        String style = form.getString("style");
        SessionManager.setFragmentStyle(style);
    }

    @POST
    @Path("add_preset")
    public String addPreset() {
        FormData form = ctx.getForm();
        String themeName = form.getString("theme_name");
        String presetName = form.getString("preset_name");
        String category = form.getString("category");
        String value = form.getString("value");
        if (value == null) {
            value = "";
        }
        try {
            return Editor.addPreset(themeName, presetName, category, value);
        } catch (Exception e) {
            throw new ThemeEditorException(e.getMessage(), e);
        }
    }

    @POST
    @Path("convert_to_preset")
    public void convertValueToPreset() {
        FormData form = ctx.getForm();
        String themeName = form.getString("theme_name");
        String presetName = form.getString("preset_name");
        String category = form.getString("category");
        String value = form.getString("value");
        if (value == null) {
            throw new ThemeEditorException("Preset value cannot be null");
        }
        try {
            Editor.convertCssValueToPreset(themeName, category, presetName,
                    value);
        } catch (Exception e) {
            throw new ThemeEditorException(e.getMessage(), e);
        }
    }

    @POST
    @Path("edit_preset")
    public void editPreset() {
        FormData form = ctx.getForm();
        String themeName = form.getString("theme_name");
        String presetName = form.getString("preset_name");
        String value = form.getString("value");
        try {
            Editor.editPreset(themeName, presetName, value);
        } catch (Exception e) {
            throw new ThemeEditorException(e.getMessage(), e);
        }
    }

    @POST
    @Path("rename_preset")
    public void renamePreset() {
        FormData form = ctx.getForm();
        String themeName = form.getString("theme_name");
        String oldName = form.getString("old_name");
        String newName = form.getString("new_name");
        try {
            Editor.renamePreset(themeName, oldName, newName);
        } catch (Exception e) {
            throw new ThemeEditorException(e.getMessage(), e);
        }
    }

    @POST
    @Path("delete_preset")
    public void deletePreset() {
        FormData form = ctx.getForm();
        String themeName = form.getString("theme_name");
        String presetName = form.getString("preset_name");
        try {
            Editor.deletePreset(themeName, presetName);
        } catch (Exception e) {
            throw new ThemeEditorException(e.getMessage(), e);
        }
    }

    @POST
    @Path("make_element_use_named_style")
    public void makeElementUseNamedStyle() {
        FormData form = ctx.getForm();
        String id = form.getString("id");
        String styleName = form.getString("style_name");
        String themeName = form.getString("theme_name");
        Element element = ThemeManager.getElementById(id);
        try {
            Editor.makeElementUseNamedStyle(element, styleName, themeName);
        } catch (Exception e) {
            throw new ThemeEditorException(e.getMessage(), e);
        }
    }

    @POST
    @Path("set_style_inheritance")
    public void makeStyleInherit() {
        FormData form = ctx.getForm();
        String styleName = form.getString("style_name");
        String ancestorName = form.getString("ancestor_name");
        String themeName = form.getString("theme_name");
        try {
            Editor.setStyleInheritance(styleName, ancestorName, themeName);
        } catch (Exception e) {
            throw new ThemeEditorException(e.getMessage(), e);
        }
    }

    @POST
    @Path("remove_style_inheritance")
    public void removeStyleInheritance() {
        FormData form = ctx.getForm();
        String styleName = form.getString("style_name");
        String themeName = form.getString("theme_name");
        try {
            Editor.removeStyleInheritance(styleName, themeName);
        } catch (Exception e) {
            throw new ThemeEditorException(e.getMessage(), e);
        }
    }

    @POST
    @Path("move_element")
    public void moveElement() {
        FormData form = ctx.getForm();
        String srcId = form.getString("src_id");
        String destId = form.getString("dest_id");
        Integer order = Integer.getInteger(form.getString("order"));
        Element srcElement = ThemeManager.getElementById(srcId);
        Element destElement = ThemeManager.getElementById(destId);
        try {
            Editor.moveElement(srcElement, destElement, order);
        } catch (Exception e) {
            throw new ThemeEditorException(e.getMessage(), e);
        }
    }

    @POST
    @Path("paste_element")
    public void pasteElement() {
        FormData form = ctx.getForm();
        String destId = form.getString("dest_id");
        String id = getClipboardElement();
        if (id == null) {
            throw new ThemeEditorException("Nothing to paste");
        }
        Element element = ThemeManager.getElementById(id);
        try {
            Editor.pasteElement(element, destId);
        } catch (Exception e) {
            throw new ThemeEditorException(e.getMessage(), e);
        }
    }

    @POST
    @Path("repair_theme")
    public void repairTheme() {
        FormData form = ctx.getForm();
        String src = form.getString("src");
        try {
            Editor.repairTheme(src);
        } catch (Exception e) {
            throw new ThemeEditorException(e.getMessage(), e);
        }
    }

    @POST
    @Path("save_theme")
    public void saveTheme() {
        FormData form = ctx.getForm();
        String src = form.getString("src");
        Integer indent = Integer.getInteger(form.getString("indent"));
        try {
            Editor.saveTheme(src);
        } catch (Exception e) {
            throw new ThemeEditorException(e.getMessage(), e);
        }
    }

    @POST
    @Path("load_theme")
    public void loadTheme() {
        FormData form = ctx.getForm();
        String src = form.getString("src");
        try {
            Editor.loadTheme(src);
        } catch (Exception e) {
            throw new ThemeEditorException(e.getMessage(), e);
        }
    }

    @POST
    @Path("delete_theme")
    public void deleteTheme() {
        FormData form = ctx.getForm();
        String src = form.getString("src");
        try {
            Editor.deleteTheme(src);
        } catch (Exception e) {
            throw new ThemeEditorException(e.getMessage(), e);
        }
    }

    @POST
    @Path("delete_page")
    public void deletePage() {
        FormData form = ctx.getForm();
        String pagePath = form.getString("page_path");
        try {
            Editor.deletePage(pagePath);
        } catch (Exception e) {
            throw new ThemeEditorException(e.getMessage(), e);
        }
    }

    @POST
    @Path("select_preset_group")
    public void selectPresetGroup() {
        FormData form = ctx.getForm();
        String group = form.getString("group");
        SessionManager.setPresetGroup(group);
    }

    @POST
    @Path("select_preset_category")
    public void selectPresetCategory() {
        FormData form = ctx.getForm();
        String category = form.getString("category");
        SessionManager.setPresetCategory(category);
    }

    @POST
    @Path("set_page_styles")
    public void setPageStyles() {
        FormData form = ctx.getForm();
        String themeName = form.getString("theme_name");
        String property_map = form.getString("property_map");
        Map propertyMap = JSONObject.fromObject(property_map);
        try {
            Editor.setPageStyles(themeName, propertyMap);
        } catch (Exception e) {
            throw new ThemeEditorException(e.getMessage(), e);
        }
    }

    @POST
    @Path("select_style_category")
    public void selectStyleCategory() {
        FormData form = ctx.getForm();
        String category = form.getString("category");
        SessionManager.setStyleCategory(category);
    }

    @POST
    @Path("select_style_edit_mode")
    public void selectStyleEditMode() {
        FormData form = ctx.getForm();
        String mode = form.getString("mode");
        SessionManager.setStyleEditMode(mode);
    }

    @POST
    @Path("toggle_css_category")
    public void toggleCssCategory() {
        FormData form = ctx.getForm();
        String name = form.getString("name");
        SessionManager.toggleCssCategory(name);
    }

    @POST
    @Path("collapse_css_categories")
    public void collapseCssCategories() {
        SessionManager.setSelectedCssCategories(new ArrayList<String>());
    }

    @POST
    @Path("expand_css_categories")
    public void expandCssCategories() {
        Properties cssStyleCategories = org.nuxeo.theme.editor.Utils.getCssStyleCategories();
        List<String> allCssCategories = (List<String>) Collections.list(cssStyleCategories.propertyNames());
        SessionManager.setSelectedCssCategories(allCssCategories);
    }

    @POST
    @Path("select_style_layer")
    public void selectStyleLayer() {
        FormData form = ctx.getForm();
        String uid = form.getString("uid");
        Style layer = (Style) ThemeManager.getFormatById(uid);
        if (layer != null) {
            SessionManager.setStyleLayerId(uid);
        }
    }

    @POST
    @Path("select_named_style")
    public void selectNamedStyle() {
        FormData form = ctx.getForm();
        String uid = form.getString("uid");
        Style style = (Style) ThemeManager.getFormatById(uid);
        if (style != null) {
            SessionManager.setNamedStyleId(uid);
        }
    }

    @POST
    @Path("select_style_selector")
    public void selectStyleSelector() {
        FormData form = ctx.getForm();
        String selector = form.getString("selector");
        SessionManager.setStyleSelector(selector);
    }

    @POST
    @Path("select_style_manager_mode")
    public void selectStyleManagerMode() {
        FormData form = ctx.getForm();
        String mode = form.getString("mode");
        SessionManager.setStyleManagerMode(mode);
    }

    @POST
    @Path("update_element_description")
    public void updateElementDescription() {
        FormData form = ctx.getForm();
        String id = form.getString("id");
        String description = form.getString("description");
        Element element = ThemeManager.getElementById(id);
        try {
            Editor.updateElementDescription(element, description);
        } catch (Exception e) {
            throw new ThemeEditorException(e.getMessage(), e);
        }
    }

    @POST
    @Path("update_element_properties")
    public void updateElementProperties() {
        FormData form = ctx.getForm();
        String id = form.getString("id");
        String property_map = form.getString("property_map");
        Map propertyMap = JSONObject.fromObject(property_map);
        Element element = ThemeManager.getElementById(id);
        try {
            Editor.updateElementProperties(element, propertyMap);
        } catch (Exception e) {
            throw new ThemeEditorException(e.getMessage(), e);
        }
    }

    @POST
    @Path("update_element_width")
    public void updateElementWidth() {
        FormData form = ctx.getForm();
        String id = form.getString("id");
        String width = form.getString("width");
        Format layout = ThemeManager.getFormatById(id);
        try {
            Editor.updateElementWidth(layout, width);
        } catch (Exception e) {
            throw new ThemeEditorException(e.getMessage(), e);
        }
    }

    @POST
    @Path("update_element_style_css")
    public void updateElementStyleCss() {
        FormData form = ctx.getForm();
        String id = form.getString("id");
        String viewName = form.getString("view_name");
        String cssSource = form.getString("css_source");
        Element element = ThemeManager.getElementById(id);
        Style selectedStyleLayer = getSelectedStyleLayer();
        try {
            Editor.updateElementStyleCss(element, selectedStyleLayer, viewName,
                    cssSource);
        } catch (Exception e) {
            throw new ThemeEditorException(e.getMessage(), e);
        }
    }

    @POST
    @Path("update_named_style_css")
    public void updateNamedStyleCss() {
        FormData form = ctx.getForm();
        String style_uid = form.getString("style_uid");
        String cssSource = form.getString("css_source");
        String themeName = form.getString("theme_name");
        Style style = (Style) ThemeManager.getFormatById(style_uid);
        try {
            Editor.updateNamedStyleCss(style, cssSource, themeName);
        } catch (Exception e) {
            throw new ThemeEditorException(e.getMessage(), e);
        }
    }

    @POST
    @Path("split_element")
    public void splitElement() {
        FormData form = ctx.getForm();
        String id = form.getString("id");
        Element element = ThemeManager.getElementById(id);
        try {
            Editor.splitElement(element);
        } catch (Exception e) {
            throw new ThemeEditorException(e.getMessage(), e);
        }
    }

    @POST
    @Path("update_element_style")
    public void updateElementStyle() {
        FormData form = ctx.getForm();
        String id = form.getString("id");
        String path = form.getString("path");
        String viewName = form.getString("view_name");
        String property_map = form.getString("property_map");
        Map propertyMap = JSONObject.fromObject(property_map);
        Element element = ThemeManager.getElementById(id);
        Style currentStyleLayer = getSelectedStyleLayer();
        try {
            Editor.updateElementStyle(element, currentStyleLayer, path,
                    viewName, propertyMap);
        } catch (Exception e) {
            throw new ThemeEditorException(e.getMessage(), e);
        }
    }

    @POST
    @Path("update_element_visibility")
    public void updateElementVisibility() {
        FormData form = ctx.getForm();
        String id = form.getString("id");
        List<String> perspectives = Arrays.asList(form.getList("perspectives"));
        boolean alwaysVisible = Boolean.valueOf(form.getString("always_visible"));
        Element element = ThemeManager.getElementById(id);
        try {
            Editor.updateElementVisibility(element, perspectives, alwaysVisible);
        } catch (Exception e) {
            throw new ThemeEditorException(e.getMessage(), e);
        }
    }

    @POST
    @Path("update_element_layout")
    public void updateElementPadding() {
        FormData form = ctx.getForm();
        String property_map = form.getString("property_map");
        Map propertyMap = JSONObject.fromObject(property_map);
        Element element = getSelectedElement();
        try {
            Editor.updateElementLayout(element, propertyMap);
        } catch (Exception e) {
            throw new ThemeEditorException(e.getMessage(), e);
        }
    }

    @POST
    @Path("update_element_widget")
    public void updateElementWidget() {
        FormData form = ctx.getForm();
        String id = form.getString("id");
        String viewName = form.getString("view_name");
        Element element = ThemeManager.getElementById(id);
        try {
            Editor.updateElementWidget(element, viewName);
        } catch (Exception e) {
            throw new ThemeEditorException(e.getMessage(), e);
        }
    }

    @POST
    @Path("delete_style_view")
    public void deleteStyleView() {
        FormData form = ctx.getForm();
        ;
        String styleUid = form.getString("style_uid");
        String viewName = form.getString("view_name");
        String themeName = form.getString("theme_name");
        Style style = (Style) ThemeManager.getFormatById(styleUid);
        try {
            Editor.deleteStyleView(style, viewName);
        } catch (Exception e) {
            throw new ThemeEditorException(e.getMessage(), e);
        }
    }

    @POST
    @Path("add_theme_to_workspace")
    public void addThemeToWorkspace() {
        FormData form = ctx.getForm();
        String name = form.getString("name");
        List<ThemeInfo> themes = SessionManager.getWorkspaceThemes();
        if (themes == null) {
            themes = new ArrayList<ThemeInfo>();
        }
        if (!themes.contains(name)) {
            String path = String.format("%s/default", name);
            themes.add(new ThemeInfo(name, path, false));
        }
        SessionManager.setWorkspaceThemes(themes);
    }

    @POST
    @Path("remove_theme_from_workspace")
    public void removeThemeFromWorkspace() {
        FormData form = ctx.getForm();
        String name = form.getString("name");
        List<ThemeInfo> themes = SessionManager.getWorkspaceThemes();
        if (themes == null) {
            themes = new ArrayList<ThemeInfo>();
        }
        if (themes.contains(name)) {
            themes.remove(name);
        }
        SessionManager.setWorkspaceThemes(themes);
    }

    @POST
    @Path("undo")
    public String undo() {
        FormData form = ctx.getForm();
        String themeName = form.getString("theme_name");
        try {
            return Editor.undo(themeName);
        } catch (Exception e) {
            throw new ThemeEditorException(e.getMessage(), e);
        }
    }

    /* API */

    public static ThemeDescriptor getThemeDescriptor(String themeName) {
        try {
            return ThemeManager.getThemeDescriptor(themeName);
        } catch (ThemeException e) {
            throw new ThemeEditorException(e.getMessage(), e);
        }
    }

    public static List<Style> getNamedStyles(String applicationPath, String name) {
        String currentThemeName = getCurrentThemeName(applicationPath, name);
        List<Style> styles = new ArrayList<Style>();
        for (Identifiable s : Manager.getThemeManager().getNamedObjects(
                currentThemeName, "style")) {
            styles.add((Style) s);
        }
        return styles;
    }

    public static List<Style> listNamedStylesDirectlyInheritingFrom(Style style) {
        List<Style> styles = new ArrayList<Style>();
        for (Format format : ThemeManager.listFormatsDirectlyInheritingFrom(style)) {
            if (format.isNamed()) {
                styles.add((Style) format);
            }
        }
        return styles;
    }

    public static Map<String, String> getPageStyles(String themeName) {
        Map<String, String> pageStyles = new LinkedHashMap<String, String>();
        List<PageElement> pages = Manager.getThemeManager().getPagesOf(
                themeName);
        if (!pages.isEmpty()) {
            for (PageElement page : pages) {
                Style namedStyle = null;
                try {
                    namedStyle = Editor.getNamedStyleOf(page);
                } catch (ThemeException e) {
                    e.printStackTrace();
                }
                String styleName = namedStyle == null ? ""
                        : namedStyle.getName();
                pageStyles.put(page.getName(), styleName);
            }
        }
        return pageStyles;
    }

    public static List<FragmentType> getFragments(String applicationPath) {
        List<FragmentType> fragments = new ArrayList<FragmentType>();
        String templateEngine = getTemplateEngine(applicationPath);
        for (Type f : Manager.getTypeRegistry().getTypes(TypeFamily.FRAGMENT)) {
            FragmentType fragmentType = (FragmentType) f;
            if (fragments.contains(fragmentType)) {
                continue;
            }
            List<ViewType> views = new ArrayList<ViewType>();
            for (ViewType viewType : ThemeManager.getViewTypesForFragmentType(fragmentType)) {
                if (templateEngine.equals(viewType.getTemplateEngine())) {
                    views.add(viewType);
                }
            }
            if (views.isEmpty()) {
                continue;
            }
            fragments.add(fragmentType);
        }
        return fragments;
    }

    public static List<ViewType> getViews(String fragmentTypeName,
            String applicationPath) {
        String templateEngine = getTemplateEngine(applicationPath);
        List<ViewType> views = new ArrayList<ViewType>();
        if (fragmentTypeName == null) {
            return views;
        }
        FragmentType fragmentType = (FragmentType) Manager.getTypeRegistry().lookup(
                TypeFamily.FRAGMENT, fragmentTypeName);
        if (fragmentType == null) {
            return views;
        }
        for (ViewType viewType : ThemeManager.getViewTypesForFragmentType(fragmentType)) {
            if (templateEngine.equals(viewType.getTemplateEngine())) {
                views.add(viewType);
            }
        }
        return views;
    }

    public static String getSelectedElementId() {
        return SessionManager.getElementId();
    }

    public static Element getSelectedElement() {
        String id = getSelectedElementId();
        if (id == null) {
            return null;
        }
        return ThemeManager.getElementById(id);
    }

    public static String getClipboardElement() {
        return SessionManager.getClipboardElementId();
    }

    public static String getClipboardPreset() {
        return SessionManager.getClipboardPresetId();
    }

    public static List<StyleLayer> getStyleLayersOfSelectedElement() {
        List<StyleLayer> layers = new ArrayList<StyleLayer>();
        Style style = getStyleOfSelectedElement();
        if (style == null) {
            return layers;
        }
        Style selectedStyleLayer = getSelectedStyleLayer();
        layers.add(new StyleLayer("This style", style.getUid(),
                style == selectedStyleLayer || selectedStyleLayer == null));
        for (Format ancestor : ThemeManager.listAncestorFormatsOf(style)) {
            layers.add(new StyleLayer(ancestor.getName(), ancestor.getUid(),
                    ancestor == selectedStyleLayer));
        }
        return layers;
    }

    public static boolean isSelectedElementAlwaysVisible() {
        Element selectedElement = getSelectedElement();
        return Manager.getPerspectiveManager().isAlwaysVisible(selectedElement);
    }

    public static List<PerspectiveType> getPerspectives() {
        return PerspectiveManager.listPerspectives();
    }

    public static List<String> getPerspectivesOfSelectedElement() {
        Element selectedElement = getSelectedElement();
        List<String> perspectives = new ArrayList<String>();
        for (PerspectiveType perspectiveType : Manager.getPerspectiveManager().getPerspectivesFor(
                selectedElement)) {
            perspectives.add(perspectiveType.name);
        }
        return perspectives;
    }

    public static String getStyleEditMode() {
        return SessionManager.getStyleEditMode();
    }

    public static List<String> getStyleSelectorsForSelectedElement() {
        Element element = getSelectedElement();
        String viewName = getViewNameOfSelectedElement();
        Style style = getStyleOfSelectedElement();
        Style selectedStyleLayer = getSelectedStyleLayer();
        List<String> selectors = new ArrayList<String>();
        if (selectedStyleLayer != null) {
            style = selectedStyleLayer;
        }
        if (style != null) {
            if (style.getName() != null) {
                viewName = "*";
            }
            Set<String> paths = style.getPathsForView(viewName);
            String current = getSelectedStyleSelector();
            if (current != null && !paths.contains(current)) {
                selectors.add(current);
            }
            for (String path : paths) {
                selectors.add(path);
            }
        }
        return selectors;
    }

    public static List<StyleFieldProperty> getStylePropertiesForSelectedElement() {
        Style style = getStyleOfSelectedElement();
        Style selectedStyleLayer = getSelectedStyleLayer();
        if (selectedStyleLayer != null) {
            style = selectedStyleLayer;
        }
        List<StyleFieldProperty> fieldProperties = new ArrayList<StyleFieldProperty>();
        if (style == null) {
            return fieldProperties;
        }
        String path = getSelectedStyleSelector();
        if (path == null) {
            return fieldProperties;
        }
        String viewName = getViewNameOfSelectedElement();
        if (style.getName() != null) {
            viewName = "*";
        }
        Properties properties = style.getPropertiesFor(viewName, path);

        int idx = 0;
        Properties cssProperties = org.nuxeo.theme.html.Utils.getCssProperties();
        if (properties != null) {
            Enumeration<?> propertyNames = properties.propertyNames();
            while (propertyNames.hasMoreElements()) {
                String name = (String) propertyNames.nextElement();
                String value = properties == null ? ""
                        : properties.getProperty(name, "");
                String type = cssProperties.getProperty(name, "");
                String id = "p" + idx;
                fieldProperties.add(new StyleFieldProperty(name, value, type,
                        id));
                idx += 1;
            }
        }
        return fieldProperties;
    }

    public static Map<String, List<StyleFieldProperty>> getAvailableStylePropertiesForSelectedElement() {
        String viewName = getViewNameOfSelectedElement();
        Style style = getStyleOfSelectedElement();
        Style selectedStyleLayer = getSelectedStyleLayer();
        if (selectedStyleLayer != null) {
            style = selectedStyleLayer;
        }
        Map<String, List<StyleFieldProperty>> styleFieldProperties = new LinkedHashMap<String, List<StyleFieldProperty>>();
        if (style == null) {
            return styleFieldProperties;
        }
        String path = getSelectedStyleSelector();
        if (path == null) {
            return styleFieldProperties;
        }
        if (style.getName() != null) {
            viewName = "*";
        }
        Properties styleProperties = style.getPropertiesFor(viewName, path);
        Properties cssProperties = org.nuxeo.theme.html.Utils.getCssProperties();
        Properties cssStyleCategories = org.nuxeo.theme.editor.Utils.getCssStyleCategories();
        Enumeration<?> cssStyleCategoryNames = cssStyleCategories.propertyNames();

        int idx = 0;
        while (cssStyleCategoryNames.hasMoreElements()) {
            String cssStyleCategoryName = (String) cssStyleCategoryNames.nextElement();
            List<StyleFieldProperty> fieldProperties = new ArrayList<StyleFieldProperty>();
            for (String name : cssStyleCategories.getProperty(
                    cssStyleCategoryName).split(",")) {
                String value = styleProperties == null ? ""
                        : styleProperties.getProperty(name, "");
                String type = cssProperties.getProperty(name, "");
                String id = "s" + idx;
                fieldProperties.add(new StyleFieldProperty(name, value, type,
                        id));
                idx += 1;
            }
            styleFieldProperties.put(cssStyleCategoryName, fieldProperties);
        }
        return styleFieldProperties;
    }

    public static String getInheritedStyleNameOfSelectedElement() {
        Style style = getStyleOfSelectedElement();
        Style ancestor = (Style) ThemeManager.getAncestorFormatOf(style);
        if (ancestor != null) {
            return ancestor.getName();
        }
        return "";
    }

    public static String getSelectedStyleSelector() {
        return SessionManager.getStyleSelector();
    }

    public static Style getSelectedStyleLayer() {
        String selectedStyleLayerId = getSelectedStyleLayerId();
        if (selectedStyleLayerId == null) {
            return null;
        }
        return (Style) ThemeManager.getFormatById(selectedStyleLayerId);
    }

    public static String getSelectedStyleLayerId() {
        return SessionManager.getStyleLayerId();
    }

    public static List<String> getSelectedCssCategories() {
        return SessionManager.getSelectedCssCategories();
    }

    public static String getSelectedNamedStyleId() {
        return SessionManager.getNamedStyleId();
    }

    public static Style getSelectedNamedStyle() {
        String selectedNamedStyleId = getSelectedNamedStyleId();
        if (selectedNamedStyleId == null) {
            return null;
        }
        return (Style) ThemeManager.getFormatById(selectedNamedStyleId);
    }

    public static Style getStyleOfSelectedElement() {
        Element element = getSelectedElement();
        if (element == null) {
            return null;
        }
        FormatType styleType = (FormatType) Manager.getTypeRegistry().lookup(
                TypeFamily.FORMAT, "style");
        return (Style) ElementFormatter.getFormatByType(element, styleType);
    }

    public static PaddingInfo getPaddingOfSelectedElement() {
        Element element = getSelectedElement();
        String top = "";
        String bottom = "";
        String left = "";
        String right = "";
        if (element != null) {
            Layout layout = (Layout) ElementFormatter.getFormatFor(element,
                    "layout");
            top = layout.getProperty("padding-top");
            bottom = layout.getProperty("padding-bottom");
            left = layout.getProperty("padding-left");
            right = layout.getProperty("padding-right");
        }
        return new PaddingInfo(top, bottom, left, right);
    }

    public static String getRenderedStylePropertiesForSelectedElement() {
        Style style = getStyleOfSelectedElement();
        Style currentStyleLayer = getSelectedStyleLayer();
        if (currentStyleLayer != null) {
            style = currentStyleLayer;
        }
        if (style == null) {
            return "";
        }
        List<String> viewNames = new ArrayList<String>();
        String viewName = getViewNameOfSelectedElement();
        if (style.getName() != null) {
            viewName = "*";
        }
        viewNames.add(viewName);
        boolean RESOLVE_PRESETS = false;
        boolean IGNORE_VIEW_NAME = true;
        boolean IGNORE_CLASSNAME = true;
        boolean INDENT = true;
        return org.nuxeo.theme.html.Utils.styleToCss(style, viewNames,
                RESOLVE_PRESETS, IGNORE_VIEW_NAME, IGNORE_CLASSNAME, INDENT);
    }

    public static String getRenderedPropertiesForNamedStyle(Style style) {
        if (style == null) {
            return "";
        }
        boolean RESOLVE_PRESETS = false;
        boolean IGNORE_VIEW_NAME = false;
        boolean IGNORE_CLASSNAME = true;
        boolean INDENT = true;
        return org.nuxeo.theme.html.Utils.styleToCss(style,
                style.getSelectorViewNames(), RESOLVE_PRESETS,
                IGNORE_VIEW_NAME, IGNORE_CLASSNAME, INDENT);
    }

    public static Widget getWidgetOfSelectedElement() {
        Element element = getSelectedElement();
        if (element == null) {
            return null;
        }
        FormatType widgetType = (FormatType) Manager.getTypeRegistry().lookup(
                TypeFamily.FORMAT, "widget");
        return (Widget) ElementFormatter.getFormatByType(element, widgetType);
    }

    public static String getViewNameOfSelectedElement() {
        Widget widget = getWidgetOfSelectedElement();
        if (widget == null) {
            return "";
        }
        return widget.getName();
    }

    public static List<String> getViewNamesForSelectedElement(
            String applicationPath) {
        Element selectedElement = getSelectedElement();
        String templateEngine = getTemplateEngine(applicationPath);
        List<String> viewNames = new ArrayList<String>();
        if (selectedElement == null) {
            return viewNames;
        }
        if (!selectedElement.getElementType().getTypeName().equals("fragment")) {
            return viewNames;
        }
        FragmentType fragmentType = ((Fragment) selectedElement).getFragmentType();
        for (ViewType viewType : ThemeManager.getViewTypesForFragmentType(fragmentType)) {
            String viewName = viewType.getViewName();
            String viewTemplateEngine = viewType.getTemplateEngine();
            if (!"*".equals(viewName)
                    && templateEngine.equals(viewTemplateEngine)) {
                viewNames.add(viewName);
            }
        }
        return viewNames;
    }

    public static List<FieldProperty> getSelectedElementProperties() {
        Element selectedElement = getSelectedElement();
        return org.nuxeo.theme.editor.Utils.getPropertiesOf(selectedElement);
    }

    /* Presets */

    public static List<String> getPresetGroupsForSelectedCategory() {
        return getPresetGroups(getSelectedStyleCategory());
    }

    public static List<String> getPresetGroups(String category) {
        List<String> groups = new ArrayList<String>();
        List<String> groupNames = new ArrayList<String>();
        for (PresetType preset : PresetManager.getGlobalPresets(null, category)) {
            String group = preset.getGroup();
            if (!groupNames.contains(group)) {
                groups.add(group);
            }
            groupNames.add(group);
        }
        return groups;
    }

    public static List<PresetInfo> getGlobalPresets(String group) {
        List<PresetInfo> presets = new ArrayList<PresetInfo>();
        for (PresetType preset : PresetManager.getGlobalPresets(group, null)) {
            presets.add(new PresetInfo(preset));
        }
        return presets;
    }

    public static List<PresetInfo> getCustomPresets(String themeName,
            String category) {
        List<PresetInfo> presets = new ArrayList<PresetInfo>();
        if ("".equals(category)) {
            category = null;
        }
        for (PresetType preset : PresetManager.getCustomPresets(themeName,
                category)) {
            presets.add(new PresetInfo(preset));
        }
        return presets;
    }

    public static List<PresetInfo> getPresetsForSelectedGroup(
            String applicationPath, String name) {
        String category = getSelectedStyleCategory();
        String group = getSelectedPresetGroup();
        String themeName = getCurrentThemeName(applicationPath, name);
        List<PresetInfo> presets = new ArrayList<PresetInfo>();
        List<PresetType> presetTypes = group == null ? PresetManager.getGlobalPresets(
                group, category)
                : PresetManager.getCustomPresets(themeName, category);
        for (PresetType preset : presetTypes) {
            presets.add(new PresetInfo(preset));
        }
        return presets;
    }

    public static String getPresetManagerMode() {
        return SessionManager.getPresetManagerMode();
    }

    public static String getStyleManagerMode() {
        return SessionManager.getStyleManagerMode();
    }

    public static List<String> getUnidentifiedPresetNames(String themeName) {
        return PresetManager.getUnidentifiedPresetNames(themeName);
    }

    public static String renderStyleView(Style style, String viewName) {
        List<String> viewNames = new ArrayList<String>();
        viewNames.add(viewName);
        return org.nuxeo.theme.html.Utils.styleToCss(style, viewNames, false,
                true, true, true);
    }

    public static List<String> getHardcodedColors(String themeName) {
        return Editor.getHardcodedColors(themeName);
    }

    public static List<String> getHardcodedImages(String themeName) {
        return Editor.getHardcodedImages(themeName);
    }

    /* Session */

    public static String getSelectedPresetGroup() {
        return SessionManager.getPresetGroup();
    }

    public static String getSelectedPresetCategory() {
        return SessionManager.getPresetCategory();
    }

    public static String getSelectedStyleCategory() {
        String category = SessionManager.getStyleCategory();
        if (category == null) {
            category = "page";
        }
        return category;
    }

    public static String getSelectedFragmentType() {
        return SessionManager.getFragmentType();
    }

    public static String getSelectedFragmentView() {
        return SessionManager.getFragmentView();
    }

    public static String getSelectedFragmentStyle() {
        return SessionManager.getFragmentStyle();
    }

    public static String getTemplateEngine(String applicationPath) {
        return ThemeManager.getTemplateEngineName(applicationPath);
    }

    public static String getDefaultTheme(String applicationPath, String name) {
        String defaultTheme = ThemeManager.getDefaultTheme(applicationPath);
        if (defaultTheme == null || defaultTheme.equals("")) {
            String moduleName = WebEngine.getActiveContext().getModule().getName();
            defaultTheme = ThemeManager.getDefaultTheme(applicationPath, name,
                    moduleName);
        }
        return defaultTheme;
    }

    public static String getCurrentPagePath(String applicationPath, String name) {
        String defaultTheme = getDefaultTheme(applicationPath, name);
        String currentPagePath = WebEngine.getActiveContext().getCookie(
                "nxthemes.theme");
        if (currentPagePath == null) {
            currentPagePath = defaultTheme;
        }
        return currentPagePath;
    }

    public static String getCurrentThemeName(String applicationPath, String name) {
        String defaultTheme = getDefaultTheme(applicationPath, name);
        String currentPagePath = WebEngine.getActiveContext().getCookie(
                "nxthemes.theme");
        if (currentPagePath == null) {
            return defaultTheme.split("/")[0];
        }
        return currentPagePath.split("/")[0];
    }

    public static List<PageInfo> getPages(String applicationPath, String name) {
        ThemeManager themeManager = Manager.getThemeManager();
        String currentPagePath = WebEngine.getActiveContext().getCookie(
                "nxthemes.theme");
        String defaultTheme = getDefaultTheme(applicationPath, name);
        String defaultPageName = defaultTheme.split("/")[1];

        List<PageInfo> pages = new ArrayList<PageInfo>();
        if (currentPagePath == null || !currentPagePath.contains("/")) {
            currentPagePath = defaultTheme;
        }

        String currentThemeName = currentPagePath.split("/")[0];
        String currentPageName = currentPagePath.split("/")[1];
        ThemeElement currentTheme = themeManager.getThemeByName(currentThemeName);

        if (currentTheme == null) {
            return pages;
        }

        boolean first = true;
        for (PageElement page : ThemeManager.getPagesOf(currentTheme)) {
            String pageName = page.getName();
            String link = String.format("%s/%s", currentThemeName, pageName);
            String className = pageName.equals(currentPageName) ? "selected"
                    : "";
            if (defaultPageName.equals(pageName)) {
                className += " default";
            }
            if (first) {
                className += " first";
                first = false;
            }
            pages.add(new PageInfo(pageName, link, className));
        }
        return pages;
    }

    public static List<ThemeInfo> getThemes(String applicationPath, String name) {
        List<ThemeInfo> themes = new ArrayList<ThemeInfo>();
        String defaultTheme = getDefaultTheme(applicationPath, name);
        String defaultThemeName = defaultTheme.split("/")[0];
        String defaultPageName = defaultTheme.split("/")[1];
        String currentThemeName = getCurrentThemeName(applicationPath, name);
        String templateEngine = getTemplateEngine(applicationPath);
        for (String themeName : ThemeManager.getThemeNames(templateEngine)) {
            String path = String.format("%s/%s", themeName, defaultPageName);
            Boolean selected = false;
            themes.add(new ThemeInfo(themeName, path, selected));
        }
        return themes;
    }

    public static ThemeManager getThemeManager() {
        return Manager.getThemeManager();
    }

    public static List<ThemeInfo> getWorkspaceThemes(String path, String name) {
        List<ThemeInfo> themes = new ArrayList<ThemeInfo>();
        String currentThemeName = getCurrentThemeName(path, name);
        String currentPagePath = getCurrentPagePath(path, currentThemeName);
        String templateEngine = getTemplateEngine(path);
        List<ThemeInfo> workspaceThemes = SessionManager.getWorkspaceThemes();
        Set<String> compatibleThemes = ThemeManager.getThemeNames(templateEngine);
        if (workspaceThemes == null) {
            workspaceThemes = new ArrayList<ThemeInfo>();
        }
        if (!workspaceThemes.contains(currentThemeName)) {
            workspaceThemes.add(new ThemeInfo(currentThemeName,
                    currentPagePath, true));
        }
        for (ThemeInfo themeInfo : workspaceThemes) {
            String themeName = themeInfo.name;
            if (compatibleThemes.contains(themeName)) {
                String pagePath = String.format("%s/default", themeName);
                themes.add(new ThemeInfo(themeName, pagePath,
                        themeName == currentThemeName));
            }
        }
        return themes;
    }

    public static void createFragmentPreview(String currentThemeName) {
        Editor.createFragmentPreview(currentThemeName);
    }

}
