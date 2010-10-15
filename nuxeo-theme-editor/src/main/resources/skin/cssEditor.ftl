<@extends src="main.ftl">

<#assign screen="css-editor" />

<#assign themeManager=This.getThemeManager()>
<#assign themes=themeManager.getThemeDescriptors()>

<@block name="title">Edit CSS</@block>

<@block name="content">



<table class="nxthemesManageScreen">
  <tr>
    <th>Skin: <#if theme_skin>${theme_skin.name}</#if></th>
  </tr>
  <tr>
  <td>

<#if theme_skin>
<#assign theme_skin_name = theme_skin.name>

  <form id="nxthemesNamedStyleCSSEditor" class="nxthemesForm" style="padding: 0"
      onsubmit="NXThemesCssEditor.updateNamedStyleCSS(this); return false">
    <input type="hidden" name="style_uid" value="#{theme_skin.uid}" />
    <input type="hidden" name="theme_name" value="${current_theme_name}" />

<#if theme_skin.customized>
  <div>
    <textarea id="namedStyleCssEditor" name="css_source" rows="15" cols="72"
   style="border: 1px solid #999; font-family: monospace; width: 100%; height: 250px; font-size: 11px;">${theme_skin_css}</textarea>
  </div>
  <div style="float: left">
    <button type="submit">Save</button>
  </div>

<#else>

   <textarea disabled="disabled" id="namedStyleCssEditor" name="css_source" rows="15" cols="72"
   style="cursor: default; border: 1px solid #eee; background-color: #fcfcfc; color: #666; font-family: monospace; width: 100%; height: 250px; font-size: 11px;">
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

<#else>
  <p>No skin available.</p>
</#if>

</td>
</tr>
</table>



</@block>
</@extends>
