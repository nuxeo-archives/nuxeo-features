

<div id="nxthemesPresetManager" class="nxthemesPresets nxthemesScreen">

<#assign themeManager=This.getThemeManager()>
<#assign themes=themeManager.getThemeDescriptors()>

<h1 class="nxthemesEditor">Set theme options</h1>

    <div class="nxthemesTabs nxthemesEditTabs">
       <ul>
         <li <#if selected_preset_category = 'background'>class="selected"</#if>>
             <a  href="javascript:NXThemesPresetManager.selectPresetCategory('background')">
             Background</a></li>
         <li <#if selected_preset_category = 'border'>class="selected"</#if>>
             <a  href="javascript:NXThemesPresetManager.selectPresetCategory('border')">
             Border</a></li>             
         <li <#if selected_preset_category = 'color'>class="selected"</#if>>
              <a href="javascript:NXThemesPresetManager.selectPresetCategory('color')">
              Color</a></li>
         <li <#if selected_preset_category = 'font'>class="selected"</#if>>
             <a  href="javascript:NXThemesPresetManager.selectPresetCategory('font')">
             Font</a></li>
         <li <#if selected_preset_category = 'image'>class="selected"</#if>>
             <a  href="javascript:NXThemesPresetManager.selectPresetCategory('image')">
             Image</a></li>
       </ul>
       <div style="clear: both"></div>
    </div>



<#assign presets = This.getCustomPresets(current_theme_name, selected_preset_category)>

<div class="nxthemesEditorFrame">

<form class="nxthemesForm">

<#list presets as preset_info>
  <p>
    <label>${preset_info.label}</label>
    <input type="text" value="${preset_info.value?replace(r'${basePath}', '${basePath}')}" />
    <span class="description">${preset_info.description}</span>
  
    <div class="preview"
       title="${preset_info.value?replace(r'${basePath}', '${basePath}')}"></div>
  </p>
</#list>

  <div>
    <button type="submit">Save options</button>
  </div>
  
</form>

</div>

</div>
