
<span style="float: left; font: bold 12px Arial; color: #eee; padding: 3px 3px 0 15px">Presets:</span>
    
<@nxthemes_button identifier="show_presets"
  controlledBy="theme buttons"
  link="javascript:NXThemesPresetManager.setEditMode('theme presets', 'preset manager')"
  label="List presets by category" />

<@nxthemes_button identifier="show_unregistered_presets"
  controlledBy="theme buttons"
  link="javascript:NXThemesPresetManager.setEditMode('unregistered presets', 'preset manager')"
  label="Find unregistered presets" />

<@nxthemes_button identifier="create_preset"
  icon="${skinPath}/img/add-14.png"
  link="javascript:NXThemesEditor.addPreset('${theme.name?js_string}', '${selected_preset_category?js_string}', 'preset manager')"
  label="Create new preset" />
  

