<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>
<%@ taglib uri="http://liferay.com/tld/ui" prefix="liferay-ui"%>
<%@ taglib uri="http://alloy.liferay.com/tld/aui" prefix="aui"%>
<portlet:defineObjects />
<portlet:actionURL var="savePreferences" name="savePreferences" />

<%
    String pqrEmail = renderRequest.getPreferences().getValue("pqr_email", "");
	String pqrSubject = renderRequest.getPreferences().getValue("pqr_subject", "");
%>

<form id="savePreferencesForm" action="<%=savePreferences%>"
	method="POST">
	<aui:input name="pqr_email"
		label="Correo electr&oacute;nico de notificaci&oacute;n"
		value="<%=pqrEmail%>" />
	<aui:input name="pqr_subject"
		label="Asunto"
		value="<%=pqrSubject%>" />
	<aui:button-row>
		<button type="submit">Guardar</button>
	</aui:button-row>
</form>