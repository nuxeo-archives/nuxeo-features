<div id="nxthemesThemeSelector">
  <form action="javascript:void(0)">
  <label style="color: #eee; font: bold 11px arial">Theme:</label>
  <select id="theme">
    <option disabled="disabled" value="">Select a theme</option>
    <#list themes as theme>
      <option <#if theme.selected> selected="selected" class="selected"</#if> 
      value="${theme.path}">${theme.name}</option>
    </#list>
  </select>
  </form>
</div>