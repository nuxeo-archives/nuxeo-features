<#assign view_mode = Context.getCookie("nxthemes.mode", "wysiwyg") />

<div style="float: left; width: 150px; margin-left: 10px;">
<@nxthemes_button identifier="view modes"
  classNames="dropList"
  controlledBy="editor buttons"
  link="javascript:NXThemesEditor.show('nxthemesViewModes')"
  label="View" />
          
<div id="nxthemesViewModes" style="position: relative; right: 0; width: 150px; top: 10px; z-index: 2; display: none"> 
  <ul class="nxthemesDropDownMenu" >
    <li <#if view_mode == "wysiwyg">class="selected"</#if>><a href="javascript:NXThemesEditor.setViewMode('wysiwyg')">Normal mode</a></li>
    <li <#if view_mode == "fragment">class="selected"</#if>><a href="javascript:NXThemesEditor.setViewMode('fragment')">Normal mode (with fragments)</a></li>
    <li <#if view_mode == "layout">class="selected"</#if>><a href="javascript:NXThemesEditor.setViewMode('layout')">Layout mode</a></li>
    <li <#if view_mode == "area-styles-page">class="selected"</#if>><a href="javascript:NXThemesEditor.setViewMode('area-styles-page')">Area style (page)</a></li>
    <li <#if view_mode == "area-styles-section">class="selected"</#if>><a href="javascript:NXThemesEditor.setViewMode('area-styles-section')">Area style (sections)</a></li> 
    <li <#if view_mode == "area-styles-cell">class="selected"</#if>><a href="javascript:NXThemesEditor.setViewMode('area-styles-cell')">Area style (cells)</a></li>             
  </ul>
</div>
</div>