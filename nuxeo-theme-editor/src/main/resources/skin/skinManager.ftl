
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


<#if current_theme && !current_theme.saveable>
  <div id="nxthemesTopBanner" style="position: absolute">
    <div class="nxthemesInfoMessage">
    <button class="nxthemesActionButton"
    onclick="NXThemesEditor.customizeTheme('${current_theme.src}', 'skin manager')">Customize theme</button>
      <img src="${basePath}/skin/nxthemes-editor/img/error.png" width="16" height="16" style="vertical-align: bottom" />
      <span>Before you can select a skin you need to customize the <strong>${current_theme.name}</strong> theme.</span>
    </div>
    <div style="clear: both"></div>
  </div>   
</#if>

