<#if theme>

<span class="nxthemesButtonHeader">Styles:</span>
    
<@nxthemes_button identifier="show_named_styles"
  controlledBy="theme buttons"
  link="javascript:NXThemesStyleManager.setEditMode('named styles')"
  classNames="selected"
  label="List by name" />
    
<@nxthemes_button identifier="show_unused_styles"
  controlledBy="theme buttons"
  link="javascript:NXThemesStyleManager.setEditMode('unused styles')"
  label="Find unused styles" />  

<@nxthemes_button identifier="create_style"
  icon="${skinPath}/img/add-14.png"
  link="javascript:NXThemesStyleEditor.createNamedStyle(null, '${theme.name}', 'style manager')"
  label="Create new style" />

</#if>