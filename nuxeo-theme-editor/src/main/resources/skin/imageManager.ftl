<@extends src="main.ftl">

<#assign screen="image-manager" />

<@block name="title">Image library</@block>

<@block name="content">

<div class="window">
<div class="title">Image library</div>
<div class="body">

<table class="nxthemesManageScreen">
  <tr>
    <th style="width: 25%;">Collection</th>
    <th style="width: 75%;">Images</th>
  </tr>
  <tr>
  <td>

<ul class="nxthemesSelector">
<#list collections as collection>
  <li>
    <a href="javascript:NXThemesEditor.selectBankCollection('${collection}', 'image manager')">
    ${collection}</a></li>
</#list>
</ul>

</td>
<td>

<div>
  <#list images as image>
    <a href="javascript:void(0)"
       onclick="NXThemesImageManager.selectImage('${current_edit_field}', '${image}')">
      <div class="nxthemesImageSingle">
        <img src="${current_bank.connectionUrl}/${image}/image" />
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


<div class="window">
<div class="title">Upload images</div>
<div class="body">
<div>
  <#if current_bank>
    <iframe id="upload_target" name="upload_target" src="" style="display: none"></iframe>


     <form id="uploadImageForm" action="${current_bank.connectionUrl}/manage/upload"
          enctype="multipart/form-data" method="post" target="upload_target">
      <p>
        <input type="file" name="file" size="30" />
        <input type="hidden" name="bank" value="${current_bank.name}" />
        <input type="hidden" name="collection" value="custom" />
        <input type="hidden" name="redirect_url" value="${Root.getPath()}/imageUploaded" />
      </p>
      <p>
      <button class="nxthemesActionButton">Upload</button>
      </p>
    </form>
    </#if>
</div>

</div>
</div>


</@block>
</@extends>
