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
<td>
<ul class="nxthemesSelector">
<#list banks as bank>
  <li <#if selected_bank && bank.name = selected_bank.name>class="selected"</#if>>
    <a href="javascript:NXThemesEditor.selectResourceBank('${bank.name}', '${screen}')">
    ${bank.name}</a></li>
</#list>
</ul>

</td>
<td>

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
  <p><label>Status</label>
    <#if current_bank && current_bank.name = selected_bank.name>
      <strong style="color: #0c0">Connected</strong>&nbsp;
     <a class="nxthemesActionButton" href="javascript:void(0)"
      onclick="NXThemesEditor.useResourceBank('${current_theme.src}', '', '${screen}')">
      Disconnect
      </a>      
    <#else>
     <strong style="color: #c00">Not connected</strong>&nbsp;
     <a class="nxthemesActionButton" href="javascript:void(0)"
      onclick="NXThemesEditor.useResourceBank('${current_theme.src}', '${selected_bank.name}', '${screen}')">
      Connect
      </a>
    </#if>
  </p>
  </#if>
  </form>


</#if>

</td>
</tr>
</table>

</div>
</div>

</@block>
</@extends>
