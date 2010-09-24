<div class="nxthemesThemeControlPanelScreen">

<div id="nxthemesThemeControlPanel" class="nxthemesThemeControlPanel">

<table style="width: 100%" class="nxthemesThemeControlPads">
  <tr>
    <td colspan="2" style="padding: 0 10px">
    <h1 style="margin: 15px 0 0 0; padding: 0">Welcome to Nuxeo Theme</h1>
    <#if current_skin_name>
      <p style="color: #666; font-style: italic; font-weight: normal;">You are currently using the <strong>${current_skin_name}</strong> skin.</p>
    </#if>
    </td>
  </tr>
  <tr>
    <td>
        <a href="javascript:NXThemesEditor.manageSkins()">
          <img src="${basePath}/skin/nxthemes-editor/img/manage-skins-32.png" width="32" height="32" />
          <div>Choose a skin</div></a>
    </td>
    <td>
        <a href="javascript:NXThemesEditor.manageThemeLayout()">
          <img src="${basePath}/skin/nxthemes-editor/img/manage-layout-32.png" width="32" height="32" />
          <div>Modify theme layout</div></a>
    </td>
  </tr>

  <tr>
    <td>
        <a href="javascript:NXThemesEditor.managePresets()">
          <img src="${basePath}/skin/nxthemes-editor/img/manage-presets-32.png" width="32" height="32" />
          <div>Set theme options</div></a>
    </td>
    <td>
        <a href="javascript:NXThemesEditor.manageAreaStyles()">
          <img src="${basePath}/skin/nxthemes-editor/img/manage-area-styles-32.png" width="32" height="32" />
          <div>Modify area styles</div></a>
    </td>
  </tr>
  <tr>
    <td>
        <a href="javascript:NXThemesEditor.manageStyles()">
          <img src="${basePath}/skin/nxthemes-editor/img/manage-styles-32.png" width="32" height="32" />
          <div>Edit CSS</div></a>
    </td>
    <td>
        <a href="javascript:NXThemesEditor.switchToCanvas()">
          <img src="${basePath}/skin/nxthemes-editor/img/expert-mode-32.png" width="32" height="32" />
          <div>Switch to expert mode</div></a>
    </td>
  </tr>
</table>

</div>
</div>
