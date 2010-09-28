<div class="nxthemesThemeControlPanelScreen">

<#assign themeManager=This.getThemeManager()>
<#assign themes=themeManager.getThemeDescriptors()>

<#if !selected_preset_category>
  <#assign selected_preset_category="background" />
</#if>

<div id="nxthemesPresetManager" class="nxthemesThemeControlPanel">

<form class="nxthemesForm" onsubmit="return false">
  <div style="text-align: center; margin-top: -80px; padding-bottom: 10px">
    <button onclick="NXThemesEditor.manageSkins()">Choose a skin</button>
    <button class="selected">Set theme options</button>
    <button onclick="NXThemesEditor.manageStyles()">Edit CSS</button>
    <button onclick="NXThemesEditor.backToControlPanel()">Finish</button>
  </div>
</form>

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

<form class="nxthemesForm"
      onsubmit="NXThemesPresetManager.updatePresets(this); return false">

    <input type="hidden" name="theme_name" value="${current_theme_name}" />

<#list presets as preset_info>
  <p>
    <label>${preset_info.label}</label>
    <input type="text"
        <#if selected_preset_category = 'color'>class="color" style="border-color: #333"</#if>
        name="preset_${preset_info.name}"
        value="${preset_info.value}" />
    <#if selected_preset_category = 'background'>
      <a class="nxthemesBrowseButton"
         href="#" onclick="NXThemesEditor.setEditorPerspective('image manager')">Browse</a>
    </#if>
    <#if selected_preset_category = 'image'>
      <a class="nxthemesBrowseButton"
         href="#" onclick="NXThemesEditor.setEditorPerspective('image manager')">Browse</a>
    </#if>
    <span class="description">${preset_info.description}</span>
  </p>
</#list>

<#if presets>
  <div>
    <button type="submit">Save options</button>
  </div>
<#else>
  <div>No options can be configured in this category.</div>
</#if>

</form>


</div>

</div>