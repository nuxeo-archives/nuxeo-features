<div>
<#assign themeManager=This.getThemeManager()>

<div id="nxthemesBrowser" class="nxthemesScreen">

  <a onclick="NXThemesEditor.editCanvas()" class="nxthemesBack">Back to canvas</a>

  <#assign themes=themeManager.getThemeDescriptors()>
  <ul class="nxthemesSelector">
  <#list themes as theme>
  <li <#if theme.name = current_theme_name>class="selected"</#if>><a href="javascript:void(0)" 
    onclick="NXThemesEditor.selectTheme('${theme.name}', 'theme manager')">
    <#if theme.customized>
      <img src="${skinPath}/img/customized-theme-16.png" width="16" height="16" />
    <#else>
      <#if theme.xmlConfigured>
        <img src="${skinPath}/img/theme-16.png" width="16" height="16" />
      </#if>
      <#if theme.custom>
        <img src="${skinPath}/img/custom-theme-16.png" width="16" height="16" />
      </#if>
    </#if>
    ${theme.name} <span style="font-size: 11px; font-style: italic; overflow: hidden">(${theme.src})</span></a></li>
  </#list>
  </ul>

  <form action="javascript:void(0)" style="margin-top: 20px">
    <div>
      <button onclick="javascript:NXThemesEditor.addTheme()">
      <img src="${skinPath}/img/add-theme-16.png" width="16" height="16" />
      Create a new theme</button>
    </div>
  </form>

</div>

</div>

