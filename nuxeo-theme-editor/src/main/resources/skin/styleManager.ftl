
<#assign themeManager=This.getThemeManager()>
<#assign themes=themeManager.getThemeDescriptors()>
<#if selected_named_style>
  <#assign selected_named_style_name = selected_named_style.name>
</#if>

<div id="nxthemesStyleManager" class="nxthemesScreen">

<h1 class="nxthemesEditor">Manage styles</h1>


<#assign found=false>

<ul class="namedStyleSelector">
<#list named_styles as style>
  <#if selected_named_style & style.uid = selected_named_style.uid>
    <#assign found=true>
  </#if>
  <li><a <#if style.name = selected_named_style_name>class="selected"</#if> href="javascript:NXThemesStyleManager.selectNamedStyle('#{style.uid}')">${style.name}</a></li>
</#list>
</ul>

<#if found>
<form id="nxthemesNamedStyleCSSEditor" class="nxthemesForm" style="padding: 0"
      onsubmit="NXThemesStyleManager.updateNamedStyleCSS(this); return false">
<div>
  <textarea id="namedStyleCssEditor" name="cssSource" rows="15" cols="72"
 style="border: 1px solid #999; width: 100%; height: 250px; font-size: 11px;">${selected_named_style_css}</textarea>
  <input type="hidden" name="style_uid" value="#{selected_named_style.uid}" />
</div>
<div>
  <button class="nxthemesRoundButton" type="submit">Save</button>
</div>
</form>
</#if>

</div>

