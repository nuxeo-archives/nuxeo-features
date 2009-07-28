
<div style="float: left; width: 150px">
<@nxthemes_button identifier="theme_actions"
  classNames="dropList"
  hover="NXThemesEditor.showMenu('nxthemesThemeActions')"
  label="Actions" />
          
<div id="nxthemesThemeActions"  
  style="position: relative; left: 0; width: 150px; top: 10px; z-index: 2; display: none"> 
  <ul class="nxthemesDropDownMenu">
    <#if theme.saveable><li><a href="javascript:void(0)" onclick="NXThemesEditor.saveTheme('${theme.src}', 2)">Save theme</a></li></#if>
    <#if theme.exportable>
      <li><a href="javascript:void(0)" onclick="window.location='${basePath}/nxthemes-editor/xml_export?src=${theme.src}&amp;download=1&amp;indent=2'">Download theme (as XML)</a></li>  
      <li><a href="javascript:void(0)" onclick="window.location='${basePath}/nxthemes-editor/xml_export?src=${theme.src}'">Show theme (as XML)</a></li>
    </#if>
    <#if theme.repairable><li><a href="javascript:void(0)" onclick="NXThemesEditor.repairTheme('${theme.src}')">Repair theme</a></li></#if>
    <#if theme.reloadable><li><a href="javascript:void(0)" onclick="NXThemesEditor.loadTheme('${theme.src}')">Restore theme</a></li></#if>
    <#if theme.custom><li><a href="javascript:void(0)" onclick="NXThemesEditor.deleteTheme('${theme.src}')">Delete theme</a></li></#if>
  </ul>
</div>
</div>