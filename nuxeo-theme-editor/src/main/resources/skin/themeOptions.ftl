<@extends src="main.ftl">

<#assign screen="theme-options" />

<#assign themeManager=This.getThemeManager()>
<#assign themes=themeManager.getThemeDescriptors()>

<@block name="title">Set theme options</@block>

<@block name="content">


<div class="window">
<div class="title">Theme options</div>
<div class="body">

<form class="nxthemesForm"
      onsubmit="NXThemesThemeOptions.updatePresets(this); return false">

    <input type="hidden" name="theme_name" value="${current_theme_name}" />

<#assign categories = ["color", "background", "font", "image"] />
<#list categories as category>
<#assign presets = This.getCustomPresets(current_theme_name, category)>

<#list presets as preset_info>
  <p>
    <label style="padding: 2px; text-align: right">${preset_info.label}&nbsp;</label>
    <input type="text"
        <#if category = 'color'>class="color" style="border-color: #333"</#if>
        id="nxthemes_preset_${preset_info.name}"
        name="preset_${preset_info.name}"
        value="${preset_info.value}" />
    <#if category = 'background' | category = 'image'>
      <a class="nxthemesActionButton"
         href="javascript:void(0)" onclick="NXThemesEditor.selectEditField('nxthemes_preset_${preset_info.name}', 'image manager')">Browse</a>
    </#if>
    <span class="description">${preset_info.description}</span>
  </p>
</#list>

</#list>

  <p>
    <button type="submit">Save</button>
  </p>

</form>
</div>
</div>



</@block>
</@extends>