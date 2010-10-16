<@extends src="base.ftl">

  <@block name="title">
      ${bank}
  </@block>

  <@block name="content">
      <h1>Bank: ${bank}</h1>

      <h2>Style collections</h2>
      <ul>
      <#list styles as style>
        <li><a href="javascript:top.navtree.openBranch('${bank}-style-${style}')">
        <img src="${skinPath}/img/collection.png" width="16" height="16" /> ${style}</a></li>
      </#list>
      </ul>

      <h2>Preset collections</h2>
      <ul>
      <#list presets as preset>
        <li><a href="javascript:top.navtree.openBranch('${bank}-preset-${preset}')">
        <img src="${skinPath}/img/collection.png" width="16" height="16" /> ${preset}</a></li>
      </#list>
      </ul>

      <h2>Image collections</h2>
      <ul>
      <#list images as image>
        <li><a href="javascript:top.navtree.openBranch('${bank}-image-${image}')">
        <img src="${skinPath}/img/collection.png" width="16" height="16" /> ${image}</a></li>
      </#list>
      </ul>
    </div>

  </@block>

</@extends>
