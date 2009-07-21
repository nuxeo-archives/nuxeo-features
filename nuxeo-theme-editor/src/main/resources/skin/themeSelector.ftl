<div id="nxthemesThemeSelector">
  <form action="javascript:void(0)">
  <label>Themes:</label>
  <select id="theme">
    <#list themes as theme>
      <option <#if theme.selected> selected="selected" class="selected"</#if> 
      value="${theme.path}">${theme.name}</option>
    </#list>
    <option value="">More themes ...</option>
  </select>
  </form>
</div>
