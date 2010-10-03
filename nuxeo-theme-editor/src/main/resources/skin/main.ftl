
<div class="nxthemesThemeControlPanelScreen">

<div id="nxthemesPresetManager" class="nxthemesThemeControlPanel">

<h1 class="nxthemesEditor"><@block name="title" /></h1>

<table class="nxthemesManageScreen">
<tr>
<td style="width: 20%; vertical-align: top">

<ul class="nxthemesSelector">
  <li <#if screen="control-panel">class="selected"</#if><a href="javascript:NXThemesEditor.backToControlPanel()">Overview</a></li>

  <li <#if screen="skin-manager">class="selected"</#if><a href="javascript:NXThemesEditor.manageSkins()">Skin</a></li>
  <li <#if screen="preset-manager">class="selected"</#if><a href="javascript:NXThemesEditor.managePresets()">Theme options</a></li>
  <li <#if screen="style-manager">class="selected"</#if><a href="javascript:NXThemesEditor.manageStyles()">CSS</a></li>

  <li <#if screen="theme-layout">class="selected"</#if><a href="javascript:NXThemesEditor.manageThemeLayout()">Theme layout</a></li>
  <li <#if screen="area-styles">class="selected"</#if><a href="javascript:NXThemesEditor.manageAreaStyles()">Area styles</a></li>

  <li <#if screen="bank-manager">class="selected"</#if><a href="javascript:NXThemesEditor.manageThemeBanks()">Theme banks</a></li>
  <li><a href="javascript:NXThemesEditor.exit()">Quit</a></li>
</ul>

</td>
<td style="width: 80%; vertical-align: top">

  <@block name="content" />

</td>
</tr>
</table>

</div>

</div>
