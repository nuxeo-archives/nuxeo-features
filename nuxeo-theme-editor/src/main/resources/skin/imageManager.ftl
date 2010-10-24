<@extends src="main.ftl">

<#assign screen="image-manager" />

<@block name="title">Image library</@block>

<@block name="content">

<style type="text/css">
.album {
  width: 100%;
  padding: 0;
  float: left;
  background-color: #f6f6f6;
  -moz-border-radius: 8px;
}

.album .imageSingle {
  float: left;
  margin: 2px;
  width: 106px;
  text-align: center;
  border: 1px solid #999;
  -moz-border-radius: 4px;
  background-color: #fff;
}

.album .imageSingle .image {
  width: 100px;
  height: 70px;
  margin: 5px;
 }

.album .imageSingle:hover {
  border: 1px solid #666;
  cursor: pointer;
}

.album .imageSingle:hover .footer {
  color: #666;
  text-decoration: none;
}

.album .imageSingle img {
  max-width: 100px;
  max-height: 70px;
  border: none;
}

</style>

<div class="window">
<div class="title">Image library</div>
<div class="body">

  <#if current_bank>
  
<table class="nxthemesManageScreen">
  <tr>
    <th style="width: 20%;">Collection</th>
    <th style="width: 80%;">Images</th>
  </tr>
  <tr>
  <td>

<ul class="nxthemesSelector">
<#list collections as collection>
  <li><a href="javascript:NXThemesEditor.selectBankCollection('${collection}', 'image manager')">
    ${collection}</a></li>
</#list>
</ul>

</td>
<td>

<div>

    <div class="album" id="imageGallery">
      <#list images as image>
        <a href="javascript:void(0)" onclick="NXThemesImageManager.selectImage('${current_edit_field}', '${image.name}')">
          <div class="imageSingle" title="${image.resource}">
            <div class="image"><img src="${current_bank.connectionUrl}/${image.collection}/image/${image.resource}" /></div>
          </div>
        </a>
      </#list>
    </div>
</div>



</td>
</tr>
</table>


  <#else>
    <p>No bank selected</p>
    <p>
      <a href="javascript:NXThemesEditor.manageThemeBanks()"
       class="nxthemesActionButton">Connect to a bank</a>
    </p>
  </#if>
  
</div>
</div>


<#if current_bank>
  
<div class="window">
<div class="title">Upload images</div>
<div class="body">
<div>

    <p>Images will be added to the Custom collection</p>
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

</div>

</div>
</div>

</#if>

</@block>
</@extends>
