<div>
<#assign themeManager=This.getThemeManager()>

<!-- preset menu -->
<@nxthemes_view resource="preset-menu.json" />     
      
<a onclick="NXThemesEditor.editCanvas()" class="nxthemesBack">Back to canvas</a>
      
<div id="nxthemesPresetBank" class="nxthemesScreen">

<table cellpadding="0" cellspacing="0" style="width: 100%"><tr>
<td style="vertical-align: top; width: 200px; padding-right: 5px;">

<ul class="nxthemesSelector">
<#list preset_groups as group>
<li <#if group = selected_preset_group>class="selected"</#if>><a href="javascript:void(0)" 
  onclick="NXThemesPresetManager.selectPresetGroup('${group}')">
  <img src="${skinPath}/img/palette-16.png" width="16" height="16" />
  ${group}</a></li>
</#list>
</ul>

</td>
<td style="padding-left: 10px; vertical-align: top;">

<#if selected_preset_group>
<!-- Palettes -->

<table cellspacing="2" cellpadding="2" style="width: 100%">
<#assign count = 0 /> 
<#assign row = 1 /> 

<#list This.getGlobalPresets(selected_preset_group) as preset_info>
<#assign row = (count % 10) +1 /> 

  <#if row == 0>
    <tr>
  </#if>
<td class="preset">

<div class="preview" title="${preset_info.value}">
<ins class="model">
  {"id": "preset_${group}_${preset_info.name}",
   "type": "preset",
   "data": {
     "id": "${preset_info.id}",   
     "group": "${selected_preset_group}",
     "name": "${preset_info.name}",
     "editable": false,
     "copyable": true,
     "pastable": false,
     "deletable": false
     }
  }
</ins>
${preset_info.preview}</div>
<div class="name">${preset_info.name}</div>

</td>

  <#if row == 10>
    </tr>
  </#if>
  
  <#assign count = count + 1/>
</#list>

<#if row < 10>
  <#list row..9 as i>
      <td></td>
  </#list>
  </tr>
</#if>
        
</table>
</#if>

</td></tr></table>

</div>

</div>

