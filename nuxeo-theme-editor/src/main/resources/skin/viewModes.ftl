<#assign view_mode = Context.getCookie("nxthemes.mode", "wysiwyg") />

<div class="nxthemesButtonSelector" style="text-align: right; margin: 3px; padding-right: 10px">
  <#if view_mode == "wysiwyg">
    <a href="javascript:NXThemesEditor.setViewMode('fragment')">show fragments</a>
  </#if>
  <#if view_mode == "fragment">
    <a href="javascript:NXThemesEditor.setViewMode('wysiwyg')" class="selected">show fragments</a>
  </#if>
  
  <a href="javascript:NXThemesEditor.setViewMode('wysiwyg')" class="<#if view_mode == "wysiwyg" || view_mode == "fragment">selected</#if>">&#171; normal view</a>

  <a href="javascript:NXThemesEditor.setViewMode('layout')" class="<#if view_mode == "layout">selected</#if>">layout mode</a>
  <a href="javascript:NXThemesEditor.setViewMode('area-styles-cell')" class="<#if view_mode == "area-styles-cell" || view_mode == "area-styles-page" || view_mode ==
 "area-styles-section" || view_mode == "area-styles-cell">selected</#if>">area styles &#187;</a>
  <#if view_mode == "area-styles-page" || view_mode == "area-styles-section" || view_mode == "area-styles-cell"> |
    <a href="javascript:NXThemesEditor.setViewMode('area-styles-page')" class="<#if view_mode == "area-styles-page">selected</#if>">page</a>
    <a href="javascript:NXThemesEditor.setViewMode('area-styles-section')" class="<#if view_mode == "area-styles-section">selected</#if>">sections</a>
    <a href="javascript:NXThemesEditor.setViewMode('area-styles-cell')" class="<#if view_mode == "area-styles-cell">selected</#if>">cells</a>
  </#if>
</div>

