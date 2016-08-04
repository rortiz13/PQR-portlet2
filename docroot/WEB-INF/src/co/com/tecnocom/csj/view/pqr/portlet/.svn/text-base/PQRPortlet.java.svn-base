package co.com.tecnocom.csj.view.pqr.portlet;

import java.io.IOException;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequestDispatcher;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.zkoss.zk.ui.http.DHtmlLayoutPortlet;

import co.com.tecnocom.csj.core.util.EmailUtil;

import com.liferay.portal.kernel.util.ParamUtil;

public class PQRPortlet extends DHtmlLayoutPortlet {
	public static final String PQR_EMAIL_PREFERENCE = "pqr_email";
	public static final String PQR_SUBJECT_PREFERENCE = "pqr_subject";
	public static final String PQR_EMAIL_HTML_LOCATION = "/pages/static/pqr/PQR_Notification.html";

	@Override
	public void init() throws PortletException {
		super.init();

		EmailUtil.INSTANCE.removeFromCache(PQR_EMAIL_HTML_LOCATION);
	}

	@Override
	protected void doEdit(RenderRequest request, RenderResponse response) throws PortletException, IOException {
		response.setContentType("text/html");

		PortletRequestDispatcher dispatcher = null;
		dispatcher = getPortletContext().getRequestDispatcher(getPortletConfig().getInitParameter("edit-template"));
		dispatcher.include(request, response);
	}

	@Override
	public void processAction(ActionRequest request, ActionResponse response) throws PortletException, IOException {
		String pqrMail = ParamUtil.getString(request, PQR_EMAIL_PREFERENCE);
		String pqrSubject = ParamUtil.getString(request, PQR_SUBJECT_PREFERENCE);

		PortletPreferences preferences = request.getPreferences();
		preferences.setValue(PQR_EMAIL_PREFERENCE, pqrMail);
		preferences.setValue(PQR_SUBJECT_PREFERENCE, pqrSubject);
		preferences.store();
	}
}
