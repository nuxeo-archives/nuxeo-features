<@extends src="main.ftl">

<#assign screen="bank-manager" />

<@block name="title">Manage theme banks</@block>

<@block name="content">

<div class="window">
<div class="title">Manage banks</div>
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
<#if selected_bank>

  <form class="nxthemesForm">
  <p><label>Bank name</label>
    <strong>${selected_bank.name}</strong>
  </p>
  <p><label>Connection URL</label>
    <strong>${selected_bank.connectionUrl}</strong>
  </p>
  <#if connected>
  <p>You are connected as bank manager</p>
  <#else>
  <p>
    <button>Connect</button>
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