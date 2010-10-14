<#assign view_mode = Context.getCookie("nxthemes.mode", "wysiwyg") />

<#setting url_escaping_charset='UTF-8'>

<@nxthemes_button identifier="view_modes"
  classNames="dropList"
  menu="nxthemesViewModes"
  label="View" />

<div id="nxthemesViewModes" style="display: none; position: absolute;
  width: 170px;
  margin-top: 10px;
  margin-left: 2px;
  z-index: 2;">
  <ul class="nxthemesDropDownMenu">
    <li<#if view_mode == "wysiwyg"> class="selected"</#if>><a href="javascript:NXThemesEditor.setViewMode('wysiwyg')">Normal view</a></li>
    <li<#if view_mode == "fragment"> class="selected"</#if>><a href="javascript:NXThemesEditor.setViewMode('fragment')">Fragments</a></li>
    <li<#if view_mode == "layout"> class="selected"</#if>><a href="javascript:NXThemesEditor.setViewMode('layout')">Page layout</a></li>
    <li<#if view_mode == "area-styles-page" || view_mode == "area-styles-section" || view_mode == "area-styles-cell"> class="selected"</#if>><a href="javascript:NXThemesEditor.setViewMode('area-styles-page')">Area styles</a></li>
  </ul>
</div>

<div class="nxthemesButtonSelector" style="dispay: none; position: absolute; top: 2px; right: 20px;">
  <#if view_mode == "area-styles-page" || view_mode == "area-styles-section" || view_mode == "area-styles-cell"> |
    <a href="javascript:NXThemesEditor.setViewMode('area-styles-page')" class="<#if view_mode == "area-styles-page">selected</#if>">page</a>
    <a href="javascript:NXThemesEditor.setViewMode('area-styles-section')" class="<#if view_mode == "area-styles-section">selected</#if>">sections</a>
    <a href="javascript:NXThemesEditor.setViewMode('area-styles-cell')" class="<#if view_mode == "area-styles-cell">selected</#if>">cells</a>
  </#if>
</div>
