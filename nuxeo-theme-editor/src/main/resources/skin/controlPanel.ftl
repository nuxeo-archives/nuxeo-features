<@extends src="main.ftl">

<#assign screen="control-panel" />

<@block name="title">Overview</@block>

<@block name="content">

<table class="nxthemesManageScreen">
<tr>
  <th style="width: 50%;">Current theme</th>
  <th style="width: 50%;">Current skin</th>
</tr>
<tr>
<td>
  <p class="nxthemesEditor">Name: <strong>${current_theme_name}</strong></p>
  <p class="nxthemesEditor">Source: <strong>${theme.src}</strong></p>
  <p class="nxthemesEditor">Last saved: <strong>${theme.lastSaved}</strong></p>
  <p class="nxthemesEditor">Compatibility:
    <strong>
    <#list theme.templateEngines as templateEngine>
      ${templateEngine}
    </#list>
    </strong>
  </p>
</td>
<td>
  <#if current_skin>
    <p class="nxthemesEditor">You are currently using the <strong>${current_skin.name}</strong> skin.</p>
    <img style="border: 1px solid #eee; padding: 10px" src="${current_skin.preview}" />
  <#else>
    <p class="nxthemesEditor">You have not selected a theme skin yet.</p>
  </#if>
</td>
</tr>
</table>

</@block>
</@extends>
