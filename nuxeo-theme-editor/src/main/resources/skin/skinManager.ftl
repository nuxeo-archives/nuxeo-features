
<div class="window">
<div class="title">Choose a skin</div>
<div class="body">

<#if current_bank>

  <#assign skins=Root.getBankSkins(current_bank.name) />
  <#if skins>
    <div style="padding: 10px 5px">
    <#list skins as skin>
      <div class="nxthemesImageSingle nxthemesImageSingle<#if current_skin_name=skin.name>Selected</#if>">
        <a href="javascript:NXThemesSkinManager.activateSkin('${current_theme.name}', '${skin.bank}', '${skin.collection}', '${skin.resource?replace('.css', '')}')">
          <img src="${current_bank.connectionUrl}/${skin.collection}/style/${skin.resource}/preview" />
          <div>${skin.name}</div>
        </a>
      </div>
    </#list>
    <div style="clear: both"></div>
    </div>
  <#else>
    <p>No skins available</p>
  </#if>

<#else>
  <p>No bank selected</p>
  <p>
    <a href="javascript:NXThemesEditor.manageThemeBanks()"
       class="nxthemesActionButton">Connect to a bank</a>
  </p>
</#if>

</div>
</div>

