<div id="nxthemesPageSelector" class="nxthemesPageTabs">
  <div class="themeName">${current_theme_name}/ </div>
  <ul>
    <#list pages as page>
      <li class='${page.className}'><span><a class="switcher" href="javascript:void(0)"
        name="${page.link}">${page.name}</a></span></li>
    </#list>
    <li><span><a title="Add a new page"
        style="font-weight: bold" href="javascript:void(0)" onclick="javascript:NXThemesEditor.addPage('${current_theme_name}')">&nbsp;+&nbsp;</a></span></li>
  </ul>
</div>
