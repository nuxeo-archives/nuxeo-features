
<div class="nxthemesThemeControlPanelScreen">

<div id="nxthemesPresetManager" class="nxthemesThemeControlPanel">

<table class="nxthemesManageScreen">
<tr>
<td style="width: 19%; vertical-align: top;">

<div class="window">
<div class="title">Basic configuration</div>
<div class="body">

<ul class="nxthemesSelector">
  <li <#if screen="control-panel">class="selected"</#if><a href="javascript:NXThemesEditor.backToDashboard()">Dashboard</a></li>
  <li <#if screen="skin-manager">class="selected"</#if><a href="javascript:NXThemesEditor.manageSkins()">Skins</a></li>
  <li <#if screen="theme-options">class="selected"</#if><a href="javascript:NXThemesEditor.setThemeOptions()">Theme options</a></li>
  <li <#if screen="css-editor">class="selected"</#if><a href="javascript:NXThemesEditor.editCss()">CSS</a></li>
</ul>
</div>
</div>

<div class="window">
<div class="title">Advanced mode</div>
<div class="body">

<ul class="nxthemesSelector">
  <li <#if screen="bank-manager">class="selected"</#if><a href="javascript:NXThemesEditor.manageThemeBanks()">Theme banks</a></li>
  <li <#if screen="bank-manager">class="selected"</#if><a href="javascript:NXThemesEditor.switchToCanvas()">Layout editor</a></li>
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
