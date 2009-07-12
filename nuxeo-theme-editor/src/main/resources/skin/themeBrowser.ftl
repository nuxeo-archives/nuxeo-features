<div id="nxthemesBrowser" class="nxthemesScreen">

  <a onclick="NXThemesEditor.editCanvas()" class="nxthemesBack">Back to canvas</a>

  <table style="width: 100%">
  <tr>
    <th style="text-align: left; width: 60%;">Available themes</th>
    <th style="text-align: left; width: 40%;">Working set content</th>
  </tr>
  <tr>
  <td style="vertical-align: top; overflow: scroll">
  
    <ul class="nxthemesSelector">
    <#list available_themes as theme>
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
  <td style="vertical-align: top; overflow: scroll">
  
    <ul class="nxthemesSelector">
      <#list workspace_themes as themeName>
        <li><a href="javascript:void(0)" 
          onclick="NXThemesEditor.removeThemeFromWorkspace('${themeName}', 'theme browser')"><img src="${skinPath}/img/theme-16.png" width="16" height="16" /> ${themeName}</a></li>
      </#list>
    </ul>
  
  </td>
  </tr>
  </table>
</div>


