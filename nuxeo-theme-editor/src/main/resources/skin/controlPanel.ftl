<@extends src="main.ftl">

<#assign screen="control-panel" />

<@block name="title">Overview</@block>

<@block name="content">

  <h2 class="nxthemesEditor">Skins</h2>
  <#if current_skin>
    <p class="nxthemesEditor">You are currently using the <strong>${current_skin.name}</strong> skin.</p>
    <img style="border: 1px solid #eee; padding: 10px" src="${current_skin.preview}" />
  <#else>
    <p class="nxthemesEditor">You have not selected a theme skin yet.</p>
  </#if>

</@block>
</@extends>
