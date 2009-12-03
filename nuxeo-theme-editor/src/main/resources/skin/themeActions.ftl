<#setting url_escaping_charset='UTF-8'>

<#if theme.saveable>
  <@nxthemes_button identifier="refresh_theme"
    controlledBy="editor buttons"
    link="javascript:NXThemesEditor.loadTheme('${theme.src?js_string}')"
    label="Refresh theme" />
<#else>
  <@nxthemes_button identifier="restore_theme"
    controlledBy="editor buttons"
    link="javascript:NXThemesEditor.loadTheme('${theme.src?js_string}', true)"
    label="Restore theme" />
</#if>

<@nxthemes_button identifier="theme_actions"
  controlledBy="editor buttons"
  classNames="dropList"
  menu="nxthemesThemeActions"
  label="More actions" />

<div id="nxthemesThemeActions" style="display: none"> 
  <ul class="nxthemesDropDownMenu">
    <li><a href="javascript:void(0)" onclick="NXThemesEditor.manageStyles()">Manage theme styles</a></li>
    <li><a href="javascript:void(0)" onclick="NXThemesEditor.managePresets()">Manage theme presets</a></li>
    <#if theme.exportable>
      <li><a href="javascript:void(0)" onclick="window.location='${basePath}/nxthemes-editor/xml_export?src=${theme.src?url}&amp;download=1&amp;indent=2'">Download theme</a></li>  
      <li><a href="javascript:void(0)" onclick="window.location='${basePath}/nxthemes-editor/xml_export?src=${theme.src?url}'">Show source XML</a></li>
    </#if>
    <#if theme.repairable><li><a href="javascript:void(0)" onclick="NXThemesEditor.repairTheme('${theme.src?js_string}')">Repair theme</a></li></#if>
    <li><a href="javascript:void(0)" onclick="NXThemesEditor.deletePage('${current_page_path?js_string}')">Delete page (${current_page_name})</a></li> 
    <#if theme.custom><li><a href="javascript:void(0)" onclick="NXThemesEditor.deleteTheme('${theme.src?js_string}')">Delete theme</a></li></#if>
  </ul>
</div>
