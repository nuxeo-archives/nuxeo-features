
<span style="float: left; font: bold 12px Arial; color: #eee; padding: 3px 3px 0 15px">Styles:</span>
    
<@nxthemes_button identifier="show_named_styles"
  controlledBy="theme buttons"
  link="javascript:NXThemesStyleManager.setEditMode('named styles')"
  label="List by name" />
    
<@nxthemes_button identifier="show_unused_styles"
  controlledBy="theme buttons"
  link="javascript:NXThemesStyleManager.setEditMode('unused styles')"
  label="Find unused styles" />  

<@nxthemes_button identifier="create_style"
  controlledBy="theme buttons"
  icon="${skinPath}/img/add-14.png"
  link="javascript:NXThemesEditor.addStyle()"
  label="Create new style" />