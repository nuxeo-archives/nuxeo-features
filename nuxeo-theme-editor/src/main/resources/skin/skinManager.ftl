
<div id="nxthemesSkinManager" class="nxthemesScreen">



<div class="album">
  <#list skins as skin>
    <a href="javascript:void(0)" 
       onclick="NXThemesSkinManager.activateSkin('${current_theme_name}', '${skin.bank}', '${skin.collection}', '${skin.resource?replace('.css', '')}')">
      <div class="imageSingle">
        <div class="image"><img src="${skin.preview}" /></div>
        <div class="footer">${skin.collection} ${skin.resource?replace('.css', '')}</div>
      </div>
    </a>
  </#list>
</div>


<#list banks as bank>
  <li <#if bank.name = selected_bank_name>class="selected"</#if>>
    <a href="javascript:NXThemesSkinManager.selectResourceBank('${bank.name}')">
    <img src="${basePath}/skin/nxthemes-editor/img/bank-16.png" width="16" height="16"/> ${bank.name}</a></li>
</#list>


</div>
