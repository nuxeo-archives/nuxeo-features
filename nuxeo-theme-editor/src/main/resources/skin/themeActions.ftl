
<@nxthemes_button identifier="canvas_editor"
  controlledBy="editor buttons"
  switchTo="editor perspectives/canvas editor"
  label="Refresh theme" />

<#if theme.saveable>
  <@nxthemes_button identifier="save"
    link="javascript:NXThemesEditor.saveTheme('${theme.src}', 2)"
    label="Save changes" />
</#if>

<@nxthemes_button identifier="theme_actions"
  classNames="dropList"
  hover="NXThemesEditor.showMenu('nxthemesThemeActions')"
  label="More actions" />

<div id="nxthemesThemeActions" style="position: absolute; width: 150px; z-index: 2; display: none"> 
  <ul class="nxthemesDropDownMenu">
    <li><a href="javascript:void(0)" onclick="NXThemesEditor.manageStyles()">Manage theme styles</a></li>
    <li><a href="javascript:void(0)" onclick="NXThemesEditor.managePresets()">Manage theme presets</a></li>
    <#if theme.exportable>
      <li><a href="javascript:void(0)" onclick="window.location='${basePath}/nxthemes-editor/xml_export?src=${theme.src}&amp;download=1&amp;indent=2'">Download theme</a></li>  
      <li><a href="javascript:void(0)" onclick="window.location='${basePath}/nxthemes-editor/xml_export?src=${theme.src}'">Show source XML</a></li>
    </#if>
    <#if theme.repairable><li><a href="javascript:void(0)" onclick="NXThemesEditor.repairTheme('${theme.src}')">Repair theme</a></li></#if>
    <#if theme.reloadable><li><a href="javascript:void(0)" onclick="NXThemesEditor.loadTheme('${theme.src}')">Restore theme from source</a></li></#if>
    <li><a href="javascript:void(0)" onclick="NXThemesEditor.deletePage('${current_page_path}')">Delete this page</a></li> 
    <#if theme.custom><li><a href="javascript:void(0)" onclick="NXThemesEditor.deleteTheme('${theme.src}')">Delete theme</a></li></#if>
  </ul>
</div>
