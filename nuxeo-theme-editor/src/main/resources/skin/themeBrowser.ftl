<div id="nxthemesBrowser" class="nxthemesScreen">

  <table style="width: 100%;" cellpadding="3" cellspacing="2">
  <tr>
    <th style="text-align: left; width: 25%; background-color: #999; color: #fff">Working list</th>
    <th style="text-align: left; width: 75%; background-color: #999; color: #fff">Available themes</th>
  </tr>
  <tr>
  <td style="vertical-align: top">

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
  <td style="vertical-align: top;">

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
  </tr>
  </table>

  <button class="nxthemesRoundButton" style="margin-top: 10px" onclick="NXThemesEditor.editCanvas()">DONE</button>
</div>


