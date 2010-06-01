
<div id="nxthemesSkinManager" class="nxthemesScreen">

<h1 class="nxthemesEditor">Manage skins</h1>

  <table class="nxthemesManageScreen">
  <tr>
    <th style="width: 25%;">Bank</th>
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

<#list skins as skin>
${skin.collection}
${skin.resource}
</#list>

</td>
</tr>
</table>

</div>
