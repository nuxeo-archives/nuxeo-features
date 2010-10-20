
<div class="nxthemesThemeControlPanelScreen">

<div id="nxthemesPresetManager" class="nxthemesThemeControlPanel">

<#if current_theme && !current_theme.saveable>
  <div class="nxthemesInfoMessage">These are factory settings for the <strong>${current_theme.name}</strong> theme.
  <button class="nxthemesActionButton"
  onclick="NXThemesEditor.customizeTheme('${current_theme.src}', '${screen}')">Customize the theme</button>.</div>
</#if>

<table style="width: 100%">
<tr>
<td style="width: 19%; vertical-align: top;">

<div class="window">
<div class="title">Basic configuration</div>
<div class="body">

<ul class="nxthemesSelector">
  <li <#if screen="control-panel">class="selected"</#if><a href="javascript:NXThemesEditor.backToDashboard()">Dashboard</a></li>
  <li <#if screen="skin-manager">class="selected"</#if><a href="javascript:NXThemesEditor.manageSkins()">Skins</a></li>
  <li <#if screen="theme-options">class="selected"</#if><a href="javascript:NXThemesEditor.setThemeOptions()">Theme options</a></li>
</ul>
</div>
</div>

<div class="window">
<div class="title">Advanced configuration</div>
<div class="body">

<ul class="nxthemesSelector">
  <li <#if screen="css-editor">class="selected"</#if><a href="javascript:NXThemesEditor.editCss()">CSS Editor</a></li>
  <li <#if screen="image-manager">class="selected"</#if><a href="javascript:NXThemesEditor.manageImages()">Image library</a></li>
  <li <#if screen="bank-manager">class="selected"</#if><a href="javascript:NXThemesEditor.manageThemeBanks()">Theme banks</a></li>
</ul>
</div>
</div>

</td>

<td style="width: 1%">
</td>


<td style="width: 80%; vertical-align: top">

  <@block name="content" />

</td>
</tr>
</table>


</div>

</div>
