<div class="nxthemesThemeControlPanelScreen">

<div id="nxthemesSkinManager" class="nxthemesThemeControlPanel">

<form class="nxthemesForm" onsubmit="return false">
  <div style="text-align: center; margin-top: -80px; padding-bottom: 10px">
    <button class="selected">Choose a skin</button>
    <button onclick="NXThemesEditor.managePresets()">Set theme options</button>
    <button onclick="NXThemesEditor.manageStyles()">Edit CSS</button>
    <button onclick="NXThemesEditor.backToControlPanel()">Finish</button>
  </div>
</form>

<h1 class="nxthemesEditor">Choose a skin</h1>

<table class="nxthemesManageScreen">
  <tr>
    <th style="width: 25%;">Theme bank</th>
    <th style="width: 75%;">Skins</th>
  </tr>
  <tr>
  <td>

<ul class="nxthemesSelector">
<#list banks as bank>
  <li <#if bank.name = selected_bank_name>class="selected"</#if>>
    <a href="javascript:NXThemesSkinManager.selectResourceBank('${bank.name}')">
    <img src="${basePath}/skin/nxthemes-editor/img/bank-16.png" width="16" height="16"/> ${bank.name}</a></li>
</#list>
</ul>

</td>
<td>

<div class="album">
  <#list skins as skin>
    <a href="javascript:void(0)"
       onclick="NXThemesSkinManager.activateSkin('${current_theme_name}', '${skin.bank}', '${skin.collection}', '${skin.resource?replace('.css', '')}')">
      <div class="imageSingle <#if current_skin_name=skin.name>selected</#if>">
        <div class="image"><img src="${skin.preview}" /></div>
        <div class="footer">${skin.name}</div>
      </div>
    </a>
  </#list>
</div>

</td>
</tr>
</table>


</div>
</div>
