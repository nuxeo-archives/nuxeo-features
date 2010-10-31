<#setting url_escaping_charset='UTF-8'>

<span style="font: bold 11px arial; padding: 3px 4px 3px 7px; color: #ccc; float: left">Dashboard actions: </span>

<#if theme.saveable>
<@nxthemes_button identifier="dashboard preview theme"
  link="javascript:NXThemesEditor.showThemePreview()"
  label="Preview theme" />
</#if>   

<#if theme.custom>
    <@nxthemes_button identifier="dashboard uncustomize theme"
  link="javascript:NXThemesEditor.uncustomizeTheme('${theme.src}', 'dashboard actions')"
  label="Uncustomize theme" />
</#if>

<#if !theme.saveable>
  <@nxthemes_button identifier="dashboard customize theme"
  link="javascript:NXThemesEditor.customizeTheme('${theme.src}', 'dashboard actions')"
  label="Customize theme" />
</#if>
