
<#if theme>

<span class="nxthemesButtonHeader">Presets:</span>
    
<@nxthemes_button identifier="show_presets"
  controlledBy="theme buttons"
  link="javascript:NXThemesPresetManager.setEditMode('theme presets')"
  classNames="selected"
  label="List presets by category" />

<@nxthemes_button identifier="show_unregistered_presets"
  controlledBy="theme buttons"
  link="javascript:NXThemesPresetManager.setEditMode('unregistered presets')"
  label="Find unregistered presets" />

<@nxthemes_button identifier="create_preset"
  icon="${skinPath}/img/add-14.png"
  link="javascript:NXThemesEditor.addPreset('${theme.name?js_string}', '${selected_preset_category?js_string}', 'preset manager')"
  label="Create new preset" />

</#if>
