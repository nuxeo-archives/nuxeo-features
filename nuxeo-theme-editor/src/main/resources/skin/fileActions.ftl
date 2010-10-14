<#setting url_escaping_charset='UTF-8'>

<#if theme>

<@nxthemes_button identifier="file_action"
  classNames="dropList"
  menu="nxthemesFileActions"
  label="File" />

<div id="nxthemesFileActions" style="display: none;position: absolute;
  width: 200px;
  margin-top: 10px;
  margin-left: 2px;
  z-index: 1;">
  <ul class="nxthemesDropDownMenu">
    <li><a href="javascript:NXThemesEditor.addTheme('theme browser')">New theme</a></li>
  <#if theme.saveable>
    <li><a href="javascript:NXThemesEditor.loadTheme('${theme.src?js_string}')">Refresh theme</a></li>
  <#else>
    <li><a href="javascript:window.location='${basePath}/nxthemes-editor/xml_export?src=${theme.src?url}&amp;download=1&amp;indent=2'">Download theme</a></li>
  </#if>

    <#if theme.exportable>
      <#if theme.saveable>
        <li><a href="javascript:window.location='${basePath}/nxthemes-editor/xml_export?src=${theme.src?url}&amp;download=1&amp;indent=2'">Download theme</a></li>
      </#if>
      <li><a href="javascript:window.location='${basePath}/nxthemes-editor/xml_export?src=${theme.src?url}'">Show source XML</a></li>
    </#if>
    <#if theme.repairable><li><a href="javascript:NXThemesEditor.repairTheme('${theme.src?js_string}')">Repair theme</a></li></#if>
    <#if !theme.saveable><li><a href="javascript:NXThemesEditor.loadTheme('${theme.src?js_string}', true)">Restore theme</a></li></#if>
    <li><a href="javascript:NXThemesEditor.deletePage('${current_page_path?js_string}')">Delete this page (${current_page_name})</a></li>
    <#if theme.custom><li><a href="javascript:NXThemesEditor.deleteTheme('${theme.src?js_string}')">Delete theme (${theme.name})</a></li></#if>
    <li><a href="javascript:NXThemesEditor.exit()">Exit</a></li>
  </ul>
</div>

</#if>
