<div class="nxthemesThemeControlPanelScreen">

<div id="nxthemesThemeControlPanel" class="nxthemesThemeControlPanel">


<table style="width: 100%" class="nxthemesThemeControlPads">
  <tr>
    <td style="vertical-align: top; width: 25%">

    <h2>Current skin</h2>
    <#if current_skin>
      <img src="${current_skin.preview}" />
       <div style="color: #666; font-style: italic; font-weight: normal;">You are currently using the <strong>${current_skin.name}</strong> skin.</div>

    </#if>
    </td>

    <td style="vertical-align: top; width: 25%">
        <h2>Beginner</h2>
        <a href="javascript:NXThemesEditor.manageSkins()">
          <img src="${basePath}/skin/nxthemes-editor/img/manage-skins-32.png" width="32" height="32" />
          <div>Choose a skin</div></a>
        <a href="javascript:NXThemesEditor.managePresets()">
          <img src="${basePath}/skin/nxthemes-editor/img/manage-presets-32.png" width="32" height="32" />
          <div>Set theme options</div></a>
        <a href="javascript:NXThemesEditor.exit()">
          <img src="${basePath}/skin/nxthemes-editor/img/quit-32.png" width="32" height="32" />
          <div>Exit theme editor</div></a>

    </td>
    <td style="vertical-align: top; width: 25%">

        <h2>Web designer</h2>
        <a href="javascript:NXThemesEditor.manageStyles()">
          <img src="${basePath}/skin/nxthemes-editor/img/manage-styles-32.png" width="32" height="32" />
          <div>Edit CSS</div></a>

        <a href="javascript:NXThemesEditor.manageThemeLayout()">
          <img src="${basePath}/skin/nxthemes-editor/img/manage-layout-32.png" width="32" height="32" />
          <div>Modify theme layout</div></a>

        <a href="javascript:NXThemesEditor.manageAreaStyles()">
          <img src="${basePath}/skin/nxthemes-editor/img/manage-area-styles-32.png" width="32" height="32" />
          <div>Modify area styles</div></a>

    </td>
    <td style="vertical-align: top; width: 25%">

        <h2>Expert</h2>
        <a href="javascript:NXThemesEditor.switchToCanvas()">
          <img src="${basePath}/skin/nxthemes-editor/img/expert-mode-32.png" width="32" height="32" />
          <div>Switch to expert mode</div></a>


    </td>
  </tr>
</table>

</div>
