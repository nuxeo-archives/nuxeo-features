<div>
<#assign themeManager=This.getThemeManager()>
<#assign themes=themeManager.getThemeDescriptors()>
<#if selected_named_style>
  <#assign selected_named_style_name = selected_named_style.name>
</#if>

<a onclick="NXThemesEditor.editCanvas()" class="nxthemesBack">Back to canvas</a>

<div id="nxthemesStyleManager" class="nxthemesScreen">

<table cellpadding="0" cellspacing="0" style="width: 100%"><tr>

<td style="padding-left: 10px; vertical-align: top;">


<p class="nxthemesEditor"><em>These styles are associated with non existing views. They can probably be cleaned up.</em><p>

<#assign styles=themeManager.getStyles(current_theme_name)>
<#list styles as style>

<#assign views=themeManager.getUnusedStyleViews(style)>
<#if views>

<#list views as view>

<form class="unusedViews" action="javascript:void(0)" submit="return false">
  <div>
    <input type="hidden" name="theme_name" value="${current_theme_name}" />
    <input type="hidden" name="style_uid" value="#{style.uid}" />
    <input type="hidden" name="view_name" value="${view}" />
  </div>
   
  <div style="font-size: 11px; font-weight: bold">
    '${view}' view
  </div>
  
  <pre style="margin: 4px 0 6px 0; font-size: 10px; background-color: #ffc; border: 1px solid #fc0">${This.renderStyleView(style, view)}</pre>

  <button type="submit">
    <img src="${skinPath}/img/cleanup-16.png" width="16" height="16" />
    Clean up
  </button>

</form>

</#list>

</#if>
</#list>


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
  <button type="submit">Save</button>
</div>
</form>
</#if>


</td></tr></table>

</div>

