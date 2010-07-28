
<div id="nxthemesStyleManager" class="nxthemesScreen">

<#assign themeManager=This.getThemeManager()>
<#assign themes=themeManager.getThemeDescriptors()>
<#if selected_named_style>
  <#assign selected_named_style_name = selected_named_style.name>
</#if>

<h1 class="nxthemesEditor">Edit CSS</h1>


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
<form id="nxthemesNamedStyleCSSEditor" class="nxthemesForm" style="padding: 0"
      onsubmit="NXThemesStyleManager.updateNamedStyleCSS(this); return false">
<div>
  <textarea id="namedStyleCssEditor" name="css_source" rows="15" cols="72"
 style="border: 1px solid #999; width: 100%; height: 250px; font-size: 11px;">${selected_named_style_css}</textarea>
  <input type="hidden" name="style_uid" value="#{selected_named_style.uid}" />
  <input type="hidden" name="theme_name" value="${current_theme_name}" />
</div>
<div>
  <button type="submit">Save</button>
</div>
</form>
</#if>

</td>
</tr>
</table>



</div>
