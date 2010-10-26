<@extends src="main.ftl">

<#assign screen="bank-manager" />

<@block name="title">Manage theme banks</@block>

<@block name="content">

<div class="window">
<div class="title">Manage banks</div>
<div class="body">

<#if banks>

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
    <a href="javascript:NXThemesEditor.selectResourceBank('${bank.name}', 'bank manager')">
    <img src="${basePath}/skin/nxthemes-editor/img/bank-16.png" />
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
  <p><label>Status</label>
    <#if current_bank && current_bank.name = selected_bank.name>
      <strong style="color: #f60;">Connected</strong>&nbsp;
     <a class="nxthemesActionButton" href="javascript:void(0)" 
        onclick="NXThemesEditor.useResourceBank('${current_theme.src}', '', 'bank manager')">
      Disconnect
      </a>      
    <#else>
     <strong style="color: #c00">&lt;Not connected&gt;</strong>&nbsp;
     <a class="nxthemesActionButton" href="javascript:void(0)" 
        onclick="NXThemesEditor.useResourceBank('${current_theme.src}', '${selected_bank.name}', 'bank manager')">
      Connect
      </a>
    </#if>
  </p>
  </form>


</#if>

</td>
</tr>
</table>

</div>
</div>

<#else>
<p>No banks have been registered</p>
</#if>

</@block>
</@extends>
