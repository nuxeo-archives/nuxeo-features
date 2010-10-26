<#setting url_escaping_charset='UTF-8'>

<@nxthemes_controller resource="dashboard-perspectives.json" />
    
<div id="canvasEditorTab">
  <a href="javascript:NXThemesEditor.backToCanvas()">
    <img src="${basePath}/skin/nxthemes-editor/img/canvas-editor-tab.png" />
  </a>
</div>

<div class="nxthemesThemeControlPanelScreen">

<#if current_theme && !current_theme.saveable>
  <div id="nxthemesTopBanner">
    <div class="nxthemesInfoMessage">
    <button class="nxthemesActionButton"
    onclick="NXThemesEditor.customizeTheme('${current_theme.src}', '${screen}')">Customize this theme</button>
      <img src="${basePath}/skin/nxthemes-editor/img/error.png" width="16" height="16" style="vertical-align: bottom" />
      <span>These are factory settings for the <strong>${current_theme.name}</strong> theme.</span>
    </div>
    <div style="clear: both"></div>
  </div>   
</#if>


<div class="nxthemesThemeControlPanel">


<table style="width: 100%">
<tr>
<td style="width: 19%; vertical-align: top;">

<div class="window">
<div class="title">Basic configuration</div>
<div class="body">

<@nxthemes_tabs identifier="dashboard basic configuration" styleClass="nxthemesDashboardMenu">
  <tab switchTo="dashboard perspectives/control panel" label="Dashboard"  />
  <tab switchTo="dashboard perspectives/skin manager" label="Skins"  />
  <tab switchTo="dashboard perspectives/theme options" label="Theme options"  />
</@nxthemes_tabs>

</div>
</div>

<div class="window">
<div class="title">Advanced configuration</div>
<div class="body">

<@nxthemes_tabs identifier="dashboard advanced configuration" styleClass="nxthemesDashboardMenu">
  <tab switchTo="dashboard perspectives/css editor" label="CSS editor"  />
  <tab switchTo="dashboard perspectives/image manager" label="Image library"  />
  <tab switchTo="dashboard perspectives/bank manager" label="Theme banks"  />
</@nxthemes_tabs>

</div>
</div>


<div class="window">
<div class="title">Export mode</div>
<div class="body">

<@nxthemes_tabs identifier="dashboard expert mode" styleClass="nxthemesDashboardMenu">
  <tab switchTo="dashboard perspectives/theme browser" label="Theme browser"  />  
  <tab switchTo="dashboard perspectives/style manager" label="Style manager"  />
  <tab switchTo="dashboard perspectives/preset manager" label="Preset manager"  />
</@nxthemes_tabs>

</div>
</div>


</td>

<td style="width: 1%">
</td>


<td style="width: 80%; vertical-align: top">

    <!-- control panel -->
    <@nxthemes_panel identifier="control panel"
      url="${basePath}/nxthemes-editor/controlPanel"
      controlledBy="dashboard perspectives"
      visibleInPerspectives="control panel" />
      
    <!-- theme options -->
    <@nxthemes_panel identifier="theme options"
      url="${basePath}/nxthemes-editor/themeOptions"
      controlledBy="dashboard perspectives,color picker"
      visibleInPerspectives="theme options" />
      
    <!-- css editor -->
    <@nxthemes_panel identifier="css editor"
      url="${basePath}/nxthemes-editor/cssEditor"
      controlledBy="dashboard perspectives"
      visibleInPerspectives="css editor" />

    <!-- skin manager -->
    <@nxthemes_panel identifier="skin manager"
      url="${basePath}/nxthemes-editor/skinManager"
      controlledBy="dashboard perspectives"
      visibleInPerspectives="skin manager" />

    <!-- bank manager -->
    <@nxthemes_panel identifier="bank manager"
      url="${basePath}/nxthemes-editor/bankManager"
      controlledBy="dashboard perspectives"
      visibleInPerspectives="bank manager" />

    <!-- image manager -->
    <@nxthemes_panel identifier="image manager"
      url="${basePath}/nxthemes-editor/imageManager"
      controlledBy="dashboard perspectives"
      visibleInPerspectives="image manager" />
      
    <!-- preset manager -->
    <@nxthemes_panel identifier="preset manager"
      url="${basePath}/nxthemes-editor/presetManager"
      controlledBy="dashboard perspectives"
      visibleInPerspectives="preset manager" />
      
    <!-- style manager -->
    <@nxthemes_controller resource="style-manager-actions.json" />
    <@nxthemes_panel identifier="style manager"
      url="${basePath}/nxthemes-editor/styleManager"
      controlledBy="dashboard perspectives,style manager actions"
      visibleInPerspectives="style manager" />

    <!-- theme browser -->
    <@nxthemes_panel identifier="theme browser"
      url="${basePath}/nxthemes-editor/themeBrowser"
      controlledBy="dashboard perspectives"
      visibleInPerspectives="theme browser" />
      
</td>
</tr>
</table>

</div>
</div>