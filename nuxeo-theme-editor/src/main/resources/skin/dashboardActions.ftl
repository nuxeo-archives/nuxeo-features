<#setting url_escaping_charset='UTF-8'>

<span style="font: bold 11px arial; padding: 3px 4px 3px 7px; color: #ccc; float: left">Dashboard actions: </span>

<#if theme>

<#if theme.custom>
    <@nxthemes_button identifier="dashboard remove customizations"
  link="javascript:NXThemesEditor.uncustomizeTheme('${theme.src}', 'dashboard actions')"
  label="Remove customizations" />
</#if>

<#if !theme.saveable>
  <@nxthemes_button identifier="dashboard customize theme"
  link="javascript:NXThemesEditor.customizeTheme('${theme.src}', 'dashboard actions')"
  label="Customize ${theme.name} theme" />
</#if>

<#if theme.custom>
    <@nxthemes_button identifier="dashboard delete theme"
  link="javascript:NXThemesEditor.deleteTheme('${theme.src}', 'dashboard actions')"
  label="Delete theme" />
</#if>

</#if>