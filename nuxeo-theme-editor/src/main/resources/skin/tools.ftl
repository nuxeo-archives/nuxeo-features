<#setting url_escaping_charset='UTF-8'>

<@nxthemes_button identifier="tools"
  classNames="dropList"
  menu="nxthemesTools"
  label="Tools" />

<div id="nxthemesTools" style="display: none;position: absolute;
  width: 200px;
  margin-top: 10px;
  margin-left: 2px;
  z-index: 1;">
  <ul class="nxthemesDropDownMenu">
    <li><a href="javascript:">Manage theme options</a></li>
    <li><a href="javascript:">Browse bank presets</a></li>
    
    <li><a href="javascript:">Edit styles (CSS)</a></li>  
    <li><a href="javascript:">Manage page styles</a></li>
    <li><a href="javascript:">Manage style dependencies</a></li>    
    <li><a href="javascript:">Clean up unused styles</a></li>
   
  </ul>
</div>


<@nxthemes_button identifier="refresh button"
  link="javascript:NXThemesEditor.refreshTheme('${theme.name?js_string}')"
  icon="${basePath}/skin/nxthemes-editor/img/refresh-14.png"
  label="Refresh" />


