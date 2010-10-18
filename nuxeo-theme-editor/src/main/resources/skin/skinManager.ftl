<@extends src="main.ftl">

<#assign screen="skin-manager" />

<@block name="title">Choose a skin</@block>

<@block name="content">

<#assign skins=Root.getBankSkins(selected_bank.name) />
<#if skins>

<div class="window">
<div class="title">Choose a skin</div>
<div class="body">

<table class="nxthemesManageScreen">
  <tr>
    <th style="width: 25%;">Theme banks</th>
    <th style="width: 75%;">Skins</th>
  </tr>
<tr>
  <td style="width: 20%">

</td>


<td style="width: 79%">

<div class="album">
  <#list skins as skin>
      <div class="imageSingle <#if current_skin_name=skin.name>imageSingleSelected</#if>">
        <div class="image"><img src="${selected_bank.connectionUrl}/${skin.collection}/style/${skin.resource}/preview" /></div>
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


</td>
</tr>
</table>

</@block>
</@extends>