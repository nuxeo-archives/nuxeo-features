<#setting url_escaping_charset='UTF-8'>

<@extends src="base.ftl">

  <@block name="title">
      ${collection}
  </@block>

  <@block name="content">

      <h1>Theme collection: ${collection}</h1>

      <#if Context.principal>
      <h2>Collection export</h2>
      <form action="${Root.getPath()}/${bank}/manage/${collection}/download" method="post">
      <p>
        <button class>Download ${collection?lower_case?replace(' ', '-')}.zip</button>
      </p>
      </form>
      </#if>
  </@block>

</@extends>