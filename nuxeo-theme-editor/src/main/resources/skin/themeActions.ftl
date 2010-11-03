<#setting url_escaping_charset='UTF-8'>

<span style="font: bold 11px arial; padding: 3px 4px 3px 7px; color: white; float: left">Theme: </span>

<#if theme>

<#if theme.saveable>
  <@nxthemes_button identifier="canvas refresh button"
    link="javascript:NXThemesEditor.loadTheme('${theme.src?js_string}')"
    icon="${basePath}/skin/nxthemes-editor/img/refresh-14.png"
    label="Refresh page" />
</#if>
  
 
<#if !theme.saveable>
  <@nxthemes_button identifier="canvas customize theme"
  link="javascript:NXThemesEditor.customizeTheme('${theme.src}', 'canvas editor')"
  label="Customize ${theme.name} theme" />
</#if>   

<#if theme.custom>
    <@nxthemes_button identifier="canvas remove customizations"
  link="javascript:NXThemesEditor.uncustomizeTheme('${theme.src}', 'canvas editor')"
  label="Remove customizations" />
</#if>

<@nxthemes_button identifier="canvas themeactions"
  classNames="dropList"
  menu="nxthemesThemeActions"
  label="More actions" />

<div id="nxthemesThemeActions" style="display: none;"> 
  <ul class="nxthemesDropDownMenu">
    <#if theme.exportable>
      <#if !theme.saveable>
        <li><a href="javascript:window.location='${basePath}/nxthemes-editor/xml_export?src=${theme.src?url}&amp;download=1&amp;indent=2'">Download theme to your computer</a></li>  
      </#if>
      <li><a href="javascript:window.location='${basePath}/nxthemes-editor/xml_export?src=${theme.src?url}'">Show theme source (XML)</a></li>
    </#if>
    <!-- <#if theme.repairable><li><a href="javascript:NXThemesEditor.repairTheme('${theme.src?js_string}')">Repair theme</a></li></#if> -->
    <li><a href="javascript:NXThemesEditor.deletePage('${current_page_path?js_string}')">Delete current page (${current_page_name})</a></li> 
    <#if !theme.saveable>
      <li><a href="javascript:NXThemesEditor.loadTheme('${theme.src?js_string})">Restore original ${theme.name} theme</a></li> 
    </#if>
  </ul>
</div>

</#if>
