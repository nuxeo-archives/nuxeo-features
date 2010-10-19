<@extends src="main.ftl">

<#assign screen="skin-manager" />

<@block name="title">Choose a skin</@block>

<@block name="content">

<#assign skins=Root.getBankSkins(current_bank.name) />
<#if skins>

<div class="window">
<div class="title">Choose a skin</div>
<div class="body">

<div style="padding: 10px 5px">
  <#list skins as skin>
    <div class="nxthemesImageSingle nxthemesImageSingle<#if current_skin_name=skin.name>Selected</#if>">
      <a href="javascript:NXThemesSkinManager.activateSkin('${current_theme_name}', '${skin.bank}', '${skin.collection}', '${skin.resource?replace('.css', '')}')">
        <img src="${current_bank.connectionUrl}/${skin.collection}/style/${skin.resource}/preview" />
        <div>${skin.name}</div>
      </a>
    </div>
  </#list>
    <div style="clear: both"></div>
</div>


</div>
</div>

<#else>
  <p>No skin available</p>
</#if>



</@block>
</@extends>
