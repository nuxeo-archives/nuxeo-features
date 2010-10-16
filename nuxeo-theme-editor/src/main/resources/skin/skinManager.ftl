<@extends src="main.ftl">

<#assign screen="skin-manager" />

<@block name="title">Choose a skin</@block>

<@block name="content">

<#list banks as bank>

<#assign skins=Root.getBankSkins(bank.name) />
<#if skins>

<div class="window">
<div class="title">Choose a skin</div>
<div class="body">

<table class="nxthemesManageScreen">
<tr>
  <td style="width: 20%">

<ul class="nxthemesSelector">
<#list banks as bank>
  <li <#if bank.name = selected_bank.name>class="selected"</#if>>
    <a href="javascript:NXThemesEditor.selectResourceBank('${bank.name}', 'bank manager')">
    <img src="${basePath}/skin/nxthemes-editor/img/bank-16.png" width="16" height="16"/> ${bank.name}</a></li>
</#list>
</ul>

</td>

  <td style="width: 1%">
  </td>

<td style="width: 79%">

<div class="album">
  <#list skins as skin>
      <div class="imageSingle <#if current_skin_name=skin.name>imageSingleSelected</#if>">
        <div class="image"><img src="${selected_bank.connectionUrl}/style/${skin.collection}/${skin.resource}/preview" /></div>
        <div class="footer">${skin.name}</div>
        <div style="padding: 5px">
          <button <#if current_skin_name=skin.name>disabled="disabled"</#if> class="nxthemesActionButton"
           onclick="NXThemesSkinManager.activateSkin('${current_theme_name}', '${skin.bank}', '${skin.collection}', '${skin.resource?replace('.css', '')}')">Activate</button>
        </div>
      </div>
  </#list>

</div>

  <div style="clear: both"></div>
</div>
</div>

<#else>
  <p>No skin available</p>
</#if>

</#list>

</td>
</tr>
</table>

</@block>
</@extends>