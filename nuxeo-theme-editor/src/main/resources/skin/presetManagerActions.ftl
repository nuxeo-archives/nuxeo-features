
<@nxthemes_button identifier="show_presets"
  controlledBy="theme buttons"
  link="javascript:NXThemesPresetManager.setEditMode('theme presets', 'preset manager')"
  label="Manage presets" />

<@nxthemes_button identifier="show_unregistered_presets"
  controlledBy="theme buttons"
  link="javascript:NXThemesPresetManager.setEditMode('unregistered presets', 'preset manager')"
  label="Register CSS properties" />

<@nxthemes_button identifier="create_preset"
  icon="${skinPath}/img/add-14.png"
  link="javascript:NXThemesEditor.addPreset('${theme.name?js_string}', ${selected_preset_category?js_string}, 'preset manager')"
  label="Create new preset" />
  

