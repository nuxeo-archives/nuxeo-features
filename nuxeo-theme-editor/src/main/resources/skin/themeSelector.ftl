<div id="nxthemesThemeSelector">
  <form action="javascript:void(0)">
  <label>Themes:</label>
  <select id="theme">
  <#list themes as theme>
    <option <#if theme.name = current_theme_name> selected="selected"</#if>
     value="${theme.path}">${theme.name}</option>
  </#list>
  </select>
  </form>
</div>
