<div class="nxthemesThemeControlPanelScreen">

<div id="nxthemesImageManager" class="nxthemesThemeControlPanel">

<h1 class="nxthemesEditor">Choose an image</h1>

<table class="nxthemesManageScreen">
  <tr>
    <th style="width: 25%;">Theme bank</th>
    <th style="width: 75%;">Images
    Upload an image
    </th>
  </tr>
  <tr>
  <td>

<ul class="nxthemesSelector">
<#list banks as bank>
  <li <#if bank.name = selected_bank.name>class="selected"</#if>>
    <a href="javascript:NXThemesSkinManager.selectResourceBank('${bank.name}')">
    <img src="${basePath}/skin/nxthemes-editor/img/bank-16.png" width="16" height="16"/> ${bank.name}</a></li>
</#list>
</ul>

</td>
<td>

<div class="album">
  <#list images as image>
    <a href="javascript:void(0)"
       onclick="NXThemesImageManager.selectImage('${current_edit_field}', '${image}')">
      <div class="imageSingle">
        <div class="image"><img src="${selected_bank.connectionUrl}/image/${image}" /></div>
        <div class="footer">${image}</div>
      </div>
    </a>
  </#list>
</div>

</td>
</tr>
</table>


</div>
</div>
