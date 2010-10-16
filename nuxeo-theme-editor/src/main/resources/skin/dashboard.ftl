<#setting url_escaping_charset='UTF-8'>

<@extends src="main.ftl">

<#assign screen="control-panel" />

<@block name="title">Dashboard</@block>

<@block name="content">

<table class="nxthemesManageScreen">

<tr>
<td style="width: 49%">

<div class="window">
<div class="title">General</div>
<div class="body">
  <p class="nxthemesEditor">Theme name: <strong>${current_theme_name}</strong></p>
  <p class="nxthemesEditor">Source: <strong>${theme.src}</strong></p>
</div>
</div>

<div class="window">
<div class="title">Skin</div>
<div class="body">
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
  <p class="nxthemesEditor">
    <button class="nxthemesActionButton"
     onclick="NXThemesEditor.manageSkins()">Change skin</button>
  </p>
</div>
</div>

</td>

<td style="width: 1%">
</td>

<td style="width: 49%">

<div class="window">
<div class="title">Theme options</div>
<div class="body">
<#assign presets = This.getCustomPresets(current_theme_name, null)>
<#list presets as preset_info>
  <p class="nxthemesEditor">
    <strong title="${preset_info.description}">${preset_info.label}</strong>:
    ${preset_info.value}
  </p>
</#list>
  <p class="nxthemesEditor">
    <button class="nxthemesActionButton"
     onclick="NXThemesEditor.setThemeOptions()">Set theme options</button>
  </p>
</div>
</div>

<div class="window">
<div class="title">CSS</div>
<div class="body">
<#assign theme_skin = Root.getThemeSkin(current_theme_name) />
<#if theme_skin & theme_skin.customized>
 <p class="nxthemesEditor">You have customized the skin's CSS</p>
<#else>
 <p class="nxthemesEditor">No CSS customization were made</p>
</#if>

  <p class="nxthemesEditor">
    <button class="nxthemesActionButton"
     onclick="NXThemesEditor.editCss()">Edit CSS</button>
  </p>

</div>
</div>

</td>
</tr>
</table>


</@block>
</@extends>
