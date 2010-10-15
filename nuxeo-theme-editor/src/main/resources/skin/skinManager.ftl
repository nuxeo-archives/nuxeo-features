<@extends src="main.ftl">

<#assign screen="skin-manager" />

<@block name="title">Choose a skin</@block>

<@block name="content">

<table class="nxthemesManageScreen">
  <tr>
    <th style="width: 75%;">Available skins</th>
    <th style="width: 25%;">Theme bank</th>
  </tr>
  <tr>
  <td>

<#if skins>

<div class="album">
  <#list skins as skin>
    <a href="javascript:void(0)"
       onclick="NXThemesSkinManager.activateSkin('${current_theme_name}', '${skin.bank}', '${skin.collection}', '${skin.resource?replace('.css', '')}')">
      <div class="imageSingle <#if current_skin_name=skin.name>imageSingleSelected</#if>">
        <div class="image"><img src="${selected_bank.connectionUrl}/style/${skin.collection}/${skin.resource}/preview" /></div>
        <div class="footer">${skin.name}</div>
      </div>
    </a>
  </#list>
</div>

<#else>

<p>No skin available</p>
</#if>
</td>

  <td>

<ul class="nxthemesSelector">
<#list banks as bank>
  <li <#if bank.name = selected_bank.name>class="selected"</#if>>
    <a href="javascript:NXThemesEditor.selectResourceBank('${bank.name}', 'skin manager')">
    <img src="${basePath}/skin/nxthemes-editor/img/bank-16.png" width="16" height="16"/> ${bank.name}</a></li>
</#list>
</ul>

</td>

</tr>
</table>

</@block>
</@extends>