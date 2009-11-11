
<#if theme.saveable>
  <@nxthemes_button identifier="save"
    controlledBy="editor buttons"
    link="javascript:NXThemesEditor.saveTheme('${theme.src?js_string}', 2)"
    label="Save changes" />
</#if>

<@nxthemes_button identifier="create_preset"
  controlledBy="editor buttons"
  link="javascript:NXThemesEditor.addPreset()"
  label="Create new preset" />

