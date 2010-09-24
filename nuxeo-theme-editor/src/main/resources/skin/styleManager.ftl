<#assign themeManager=This.getThemeManager()>
<#assign themes=themeManager.getThemeDescriptors()>
<#if selected_named_style>
  <#assign selected_named_style_name = selected_named_style.name>
</#if>

<div class="nxthemesThemeControlPanelScreen">

<div id="nxthemesStyleManager" class="nxthemesThemeControlPanel">

<form class="nxthemesForm" onsubmit="return false">
  <div style="text-align: center; margin-top: -80px; padding-bottom: 10px">
    <button onclick="NXThemesEditor.manageSkins()">Choose a skin</button>
    <button onclick="NXThemesEditor.managePresets()">Set theme options</button>
    <button class="selected">Edit CSS</button>
    <button onclick="NXThemesEditor.backToControlPanel()">Finish</button>
  </div>
</form>

  <table class="nxthemesManageScreen">
  <tr>
    <th style="width: 25%;">Style</th>
    <th style="width: 75%;">CSS properties</th>
  </tr>
  <tr>
  <td>

<ul class="nxthemesSelector">
<#list named_styles as style>
  <li <#if style.name = selected_named_style_name>class="selected"</#if>>
    <a href="javascript:NXThemesStyleManager.selectNamedStyle('#{style.uid}')">
    <img src="${basePath}/skin/nxthemes-editor/img/style-16.png" width="16" height="16"/> ${style.name}</a></li>
</#list>
</ul>

</td>
<td>

<#if selected_named_style>

  <h2 class="nxthemesEditor">${selected_named_style.name}</h2>

  <form id="nxthemesNamedStyleCSSEditor" class="nxthemesForm" style="padding: 0"
      onsubmit="NXThemesStyleManager.updateNamedStyleCSS(this); return false">
    <input type="hidden" name="style_uid" value="#{selected_named_style.uid}" />
    <input type="hidden" name="theme_name" value="${current_theme_name}" />

<#if selected_named_style.customized>
  <div>
    <textarea id="namedStyleCssEditor" name="css_source" rows="15" cols="72"
   style="border: 1px solid #999; font-family: monospace; width: 100%; height: 250px; font-size: 11px;">${selected_named_style_css}</textarea>
  </div>
  <div>
    <button type="submit">Save</button>
  </div>

<#else>

   <textarea disabled="disabled" id="namedStyleCssEditor" name="css_source" rows="15" cols="72"
   style="cursor: default; border: 1px solid #eee; background-color: #fcfcfc; color: #666; font-family: monospace; width: 100%; height: 250px; font-size: 11px;">
${selected_named_style_css}
</textarea>

  <div>
    <button type="submit">Customize CSS</button>
  </div>

</#if>
</form>


<#if selected_named_style.remote & selected_named_style.customized>
 <form class="nxthemesForm" style="padding: 0; float: right"
      onsubmit="NXThemesStyleManager.restoreNamedStyle(this); return false">
    <input type="hidden" name="style_uid" value="#{selected_named_style.uid}" />
    <input type="hidden" name="theme_name" value="${current_theme_name}" />
  <div>
    <button type="submit">Restore CSS</button>
  </div>
  </form>
</#if>

</#if>

</td>
</tr>
</table>

</div>
</div>
