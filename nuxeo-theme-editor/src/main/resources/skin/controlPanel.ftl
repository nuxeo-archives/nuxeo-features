<#setting url_escaping_charset='UTF-8'>

<@extends src="main.ftl">

<#assign screen="control-panel" />

<@block name="title">Overview</@block>

<@block name="content">

<table class="nxthemesManageScreen">
<tr>
  <th style="width: 40%;">Theme details</th>
  <th style="width: 60%;">Customizations</th>
</tr>
<tr>
<td>

  <fieldset><legend>General</legend>
  <p class="nxthemesEditor">Theme name: <strong>${current_theme_name}</strong></p>
  <p class="nxthemesEditor">Source: <strong>${theme.src}</strong></p>
  </fieldset>

  <fieldset><legend>Skin</legend>
  <#if current_skin>
    <#assign bank=Root.getResourceBank(current_skin.bank) />
    <p class="nxthemesEditor">Current skin: <strong>${current_skin.name}</strong>
    <div style="margin: 10px;">
    <img style="border: 1px solid #ccc;" src="${bank.connectionUrl}/style/${current_skin.collection}/${current_skin.resource}/preview"" />
    <div>
    </p>
  <#else>
    <p class="nxthemesEditor">You have not selected a theme skin yet.</p>
  </#if>
  </legend>

</td>
<td>

<fieldset><legend>Theme options</legend>
<#assign presets = This.getCustomPresets(current_theme_name, null)>
<#list presets as preset_info>
  <p>
    <strong title="${preset_info.description}">${preset_info.label}</strong>:
    ${preset_info.value}
  </p>
</#list>
</fieldset>

<fieldset><legend>CSS</legend>
<#assign theme_skin = Root.getThemeSkin(current_theme_name) />
<#if theme_skin & theme_skin.customized>
 <p class="nxthemesEditor">You have customized the skin's CSS</p>
<#else>
 <p class="nxthemesEditor">No CSS customization were made</p>
</#if>
</fieldset>

</td>
</tr>
</table>

</@block>
</@extends>
