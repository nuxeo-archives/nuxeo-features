<div class="nxthemesPageTabs">
  <div class="themeName">${current_theme_name}</div>
  <ul>
    <#list pages as page>
      <li class='${page.className}'><span><a class="switcher" href="javascript:void(0)"
        name="${page.link}">${page.name}</a></span></li>
    </#list>
    <li><span><a style="font-weight: bold" href="javascript:void(0)" onclick="javascript:NXThemesEditor.addPage('${current_theme_name}')">+</a></span></li>
  </ul>
</div>
