
<#assign themeManager=This.getThemeManager()>
<#assign themes=themeManager.getThemeDescriptors()>

<div class="window">
<div class="title">Edit CSS</div>
<div class="body">

<#if current_bank>

<#if theme_skin>
<#assign theme_skin_name = theme_skin.name>

  <form id="nxthemesNamedStyleCSSEditor" class="nxthemesForm" style="padding: 0"
      onsubmit="NXThemesCssEditor.updateNamedStyleCSS(this); return false">
    <input type="hidden" name="style_uid" value="#{theme_skin.uid}" />
    <input type="hidden" name="theme_name" value="${current_theme_name}" />

<#if theme_skin.customized>
  <div>
    <textarea id="namedStyleCssEditor" name="css_source" rows="15" cols="72"
   style="margin-bottom: 10px; border: none; font-family: monospace; width: 100%; height: 250px; font-size: 11px;">${theme_skin_css}</textarea>
  </div>
  <div style="float: left">
    <button type="submit">Save changes</button>
  </div>

<#else>

   <textarea disabled="disabled" id="namedStyleCssEditor" name="css_source" rows="15" cols="72"
   style="margin-bottom: 10px; cursor: default; border: none; color: #999; font-family: monospace; width: 100%; height: 250px; font-size: 11px;">
${theme_skin_css}
</textarea>

  <div style="float: left">
    <button type="submit">Customize CSS</button>
  </div>

</#if>
</form>

<#if theme_skin.remote & theme_skin.customized>
  <form class="nxthemesForm" style="padding: 0; float: right"
      onsubmit="NXThemesCssEditor.restoreNamedStyle(this); return false">
    <input type="hidden" name="style_uid" value="#{theme_skin.uid}" />
    <input type="hidden" name="theme_name" value="${current_theme_name}" />
    <div>
    <button type="submit">Restore CSS</button>
    </div>
  </form>
</#if>
<div style="clear: both; padding: 5px"></div>

<#else>
  <p>No skin selected.</p>
  <p>
    <a href="javascript:NXThemesEditor.manageSkins()"
       class="nxthemesActionButton">Select a skin</a>
  </p>  
</#if>

<#else>
  <p>No bank selected</p>
  <p>
    <a href="javascript:NXThemesEditor.manageThemeBanks()"
       class="nxthemesActionButton">Connect to a bank</a>
  </p>
</#if>

</div>
</div>


<#if current_theme && !current_theme.saveable>
  <div id="nxthemesTopBanner" style="position: absolute">
    <div class="nxthemesInfoMessage">
    <button class="nxthemesActionButton"
    onclick="NXThemesEditor.customizeTheme('${current_theme.src}', 'css editor')">Customize theme</button>
      <img src="${basePath}/skin/nxthemes-editor/img/error.png" width="16" height="16" style="vertical-align: bottom" />
      <span>Before you can edit the CSS file you need to customize the <strong>${current_theme.name}</strong> theme.</span>
    </div>
    <div style="clear: both"></div>
  </div>   
</#if>