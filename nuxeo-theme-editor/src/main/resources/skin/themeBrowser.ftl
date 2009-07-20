<div id="nxthemesBrowser" class="nxthemesScreen">

  <a onclick="NXThemesEditor.editCanvas()" class="nxthemesBack">Back to canvas</a>

  <table style="width: 100%">
  <tr>
    <th style="text-align: left; width: 60%;">Available themes</th>
    <th style="text-align: left; width: 40%;">Working list</th>
  </tr>
  <tr>
  <td style="vertical-align: top; overflow: scroll">
  
    <ul class="nxthemesSelector">
    <#list available_themes as theme>
    <li><a href="javascript:void(0)" 
      onclick="NXThemesEditor.addThemeToWorkspace('${theme.name}', 'theme browser')">
       <img src="${skinPath}/img/theme-16.png" width="16" height="16" />
      ${theme.name} <span style="font-size: 11px; font-style: italic; overflow: hidden">(${theme.src})</span>
      <span class="info"><img src="${skinPath}/img/add-theme-to-list-16.png" width="16" height="16" /> add to list</span></a></li>
    </#list>
    </ul>

  </td>
  <td style="vertical-align: top; overflow: scroll">
  
    <ul class="nxthemesSelector">
      <#list workspace_themes as theme>
        <li <#if theme.selected>class="disabled"</#if>>
          <a  
            <#if !theme.selected>
            onclick="NXThemesEditor.removeThemeFromWorkspace('${theme.name}', 'theme browser')"
            </#if> 
            href="javascript:void(0)">
            <img src="${skinPath}/img/theme-16.png" width="16" height="16" /> ${theme.name}
            <span class="info"><img src="${skinPath}/img/remove-theme-from-list-16.png" width="16" height="16" />  remove from list</span></a></li>
      </#list>
    </ul>
  
  </td>
  </tr>
  </table>
</div>


