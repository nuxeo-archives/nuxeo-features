<div>
<#assign themeManager=This.getThemeManager()>

<div id="nxthemesBrowser" class="nxthemesScreen">

  <a onclick="NXThemesEditor.editCanvas()" class="nxthemesBack">Back to canvas</a>

  <table style="width: 100%">
  <tr>
  <td style="vertical-align: top; width: 50%; overflow: scroll">
  
  <#assign themes=themeManager.getThemeDescriptors()>
  <ul class="nxthemesSelector">
  <#list themes as theme>
  <li><a href="javascript:void(0)" 
    onclick="NXThemesEditor.addThemeToWorkspace('${theme.name}', 'theme browser')">
    <#if theme.customized>
      <img src="${skinPath}/img/customized-theme-16.png" width="16" height="16" />
    <#else>
      <#if theme.xmlConfigured>
        <img src="${skinPath}/img/theme-16.png" width="16" height="16" />
      </#if>
      <#if theme.custom>
        <img src="${skinPath}/img/custom-theme-16.png" width="16" height="16" />
      </#if>
    </#if>
    ${theme.name} <span style="font-size: 11px; font-style: italic; overflow: hidden">(${theme.src})</span></a></li>
  </#list>
  </ul>

  </td>
  <td style="vertical-align: top; width: 50%; overflow: scroll">
  
  <ul class="nxthemesSelector">
    <#list workspace_themes as theme>
      <li><a href="javascript:void(0)"><img src="${skinPath}/img/theme-16.png" width="16" height="16" /> ${theme.name}</a></li>
    </#list>
  </ul>
  
  </td>
  </tr>
  </table>
</div>

</div>

