<@extends src="main.ftl">

<#assign screen="bank-manager" />

<@block name="title">Manage theme banks</@block>

<@block name="content">

<div class="window">
<div class="title">Manage banks</div>
<div class="body">

<table class="nxthemesManageScreen">
  <tr>
    <th style="width: 20%;">Theme banks</th>
    <th style="width: 80%;">Settings</th>
  </tr>
<tr>

<ul class="nxthemesSelector">
<#list banks as bank>
  <li <#if bank.name = current_bank.name>class="selected"</#if>>
    <a href="javascript:NXThemesEditor.selectResourceBank('${bank.name}', 'bank manager')">
    <img src="${basePath}/skin/nxthemes-editor/img/bank-16.png" width="16" height="16"/> ${bank.name}</a></li>
</#list>
</ul>

</td>
<td>

<#if current_bank>

  <form class="nxthemesForm">
  <p><label>Bank name</label>
    <strong>${current_bank.name}</strong>
  </p>
  <p><label>Connection URL</label>
    <strong>${current_bank.connectionUrl}</strong>
  </p>
  <#if connected>
  <p>You are connected as bank manager</p>
  <#else>
  <p>
    <a class="nxthemesActionButton" href="${current_bank.connectionUrl}">Connect</a>
  </p>
  </#if>
  </form>

<#else>

  <p>No theme bank available.</p>

</#if>

</td>
</tr>
</table>

</div>
</div>

</@block>
</@extends>
