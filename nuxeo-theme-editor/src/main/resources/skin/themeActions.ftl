<#setting url_escaping_charset='UTF-8'>

<#if theme>

<@nxthemes_button identifier="refresh button"
  link="javascript:NXThemesEditor.refreshTheme('${theme.name?js_string}')"
  icon="${basePath}/skin/nxthemes-editor/img/refresh-14.png"
  label="Refresh" />
    
<@nxthemes_button identifier="manage_presets"
  link="javascript:NXThemesEditor.managePresets()"
  label="Manage presets" />
  
<@nxthemes_button identifier="manage_styles"
  link="javascript:NXThemesEditor.manageStyles()"
  label="Manage styles" />

<@nxthemes_button identifier="theme_actions"
  classNames="dropList"
  menu="nxthemesThemeActions"
  label="More actions" />

<div id="nxthemesThemeActions" style="display: none;"> 
  <ul class="nxthemesDropDownMenu">
    <#if theme.exportable>
      <#if !theme.saveable>
        <li><a href="javascript:window.location='${basePath}/nxthemes-editor/xml_export?src=${theme.src?url}&amp;download=1&amp;indent=2'">Download theme</a></li>  
      </#if>
      <li><a href="javascript:window.location='${basePath}/nxthemes-editor/xml_export?src=${theme.src?url}'">Show source XML</a></li>
    </#if>
    <!-- <#if theme.repairable><li><a href="javascript:NXThemesEditor.repairTheme('${theme.src?js_string}')">Repair theme</a></li></#if> -->
    <#if !theme.saveable><li><a href="javascript:NXThemesEditor.loadTheme('${theme.src?js_string}', true)">Restore theme</a></li></#if>
    <li><a href="javascript:NXThemesEditor.deletePage('${current_page_path?js_string}')">Delete page (${current_page_name})</a></li> 
    <#if theme.custom><li><a href="javascript:NXThemesEditor.deleteTheme('${theme.src?js_string}')">Delete theme (${theme.name})</a></li></#if>
  </ul>
</div>

</#if>
