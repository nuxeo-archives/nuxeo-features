<div id="nxthemesBrowser" class="nxthemesScreen">

  <h1 class="nxthemesEditor">Theme browser</h1>
  
  <table style="width: 100%;" cellpadding="3" cellspacing="2">
  <tr>
    <th style="text-align: left; width: 50%; background-color: #999; color: #fff">Available themes</th>
    <th style="text-align: left; width: 50%; background-color: #999; color: #fff">Working list</th>
  </tr>
  <tr>
  <td style="vertical-align: top">

    <ul class="nxthemesSelector">
    <#list available_themes as theme>
    <li><a title="${theme.src}" href="javascript:void(0)" 
      onclick="NXThemesEditor.addThemeToWorkspace('${theme.name?js_string}', 'theme browser')">
       <img src="${skinPath}/img/theme-16.png" width="16" height="16" />
      <span>${theme.name}</span>
      <span class="info"><img src="${skinPath}/img/add-theme-to-list-16.png" width="16" height="16" /> add to list</span></a></li>
    </#list>
    </ul>

  </td>
  <td style="vertical-align: top;">
    
    <ul class="nxthemesSelector">
      <#list workspace_themes as theme>
        <li <#if theme.selected>class="disabled"</#if>>
          <a
            <#if !theme.selected>
            onclick="NXThemesEditor.removeThemeFromWorkspace('${theme.name?js_string}', 'theme browser')"
            </#if> 
            href="javascript:void(0)">
            <img src="${skinPath}/img/theme-16.png" width="16" height="16" /> 
            <span>${theme.name}</span>
            <span class="info"><img src="${skinPath}/img/remove-theme-from-list-16.png" width="16" height="16" />  remove from list</span></a></li>
      </#list>
    </ul>
  
  </td>
  </tr>
  </table>

</div>
