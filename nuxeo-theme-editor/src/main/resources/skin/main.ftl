
<div class="nxthemesThemeControlPanelScreen">

<div id="nxthemesPresetManager" class="nxthemesThemeControlPanel">

<h1 class="nxthemesEditor"><@block name="title" /></h1>

<table class="nxthemesManageScreen">
<tr>
<td style="width: 20%; vertical-align: top">

<ul class="nxthemesSelector">
  <li <#if screen="control-panel">class="selected"</#if><a href="javascript:NXThemesEditor.backToControlPanel()">Overview</a></li>

  <li <#if screen="skin-manager">class="selected"</#if><a href="javascript:NXThemesEditor.manageSkins()">Choose a skin</a></li>
  <li <#if screen="preset-manager">class="selected"</#if><a href="javascript:NXThemesEditor.managePresets()">Set theme options</a></li>
  <li <#if screen="style-manager">class="selected"</#if><a href="javascript:NXThemesEditor.manageStyles()">Edit CSS</a></li>

  <li <#if screen="theme-layout">class="selected"</#if><a href="javascript:NXThemesEditor.manageThemeLayout()">Modify theme layout</a></li>
  <li <#if screen="area-styles">class="selected"</#if><a href="javascript:NXThemesEditor.manageAreaStyles()">Manage area styles</a></li>

  <li><a href="javascript:NXThemesEditor.exit()">Exit theme editor</a></li>
</ul>

</td>
<td style="width: 80%; vertical-align: top">

  <@block name="content" />

</td>
</tr>
</table>

</div>

</div>
