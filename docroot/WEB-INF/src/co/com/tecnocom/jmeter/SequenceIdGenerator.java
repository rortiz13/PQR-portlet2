package co.com.tecnocom.jmeter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.zkoss.web.portlet.RenderHttpServletRequest;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.metainfo.ComponentInfo;
import org.zkoss.zk.ui.sys.IdGenerator;

import com.liferay.portal.util.PortalUtil;

import co.com.tecnocom.csj.view.pqr.util.LiferayUtil;

public class SequenceIdGenerator implements IdGenerator {
//	public String nextComponentUuid(Desktop desktop, Component comp) {
//		String number;
//		if ((number = (String) desktop.getAttribute("Id_Num")) == null) {
//			number = "0";
//			desktop.setAttribute("Id_Num", number);
//		}
//		int i = Integer.parseInt(number);
//		i++;// Start from 1
//		desktop.setAttribute("Id_Num", String.valueOf(i));
//		System.out.println("");
//		return "t_" + i;
//	}

//	private static int id = 1;
	
	public String nextDesktopId(Desktop desktop) {
		HttpServletRequest req = (HttpServletRequest) Executions.getCurrent().getNativeRequest();
		
		String dtid = PortalUtil.getOriginalServletRequest(PortalUtil.getHttpServletRequest(((RenderHttpServletRequest) Executions.getCurrent().getNativeRequest()).getRenderRequest())).getParameter("tdtid");
//		System.out.println(dtid);
		
//		System.out.println("Desktop ID: " + Executions.getCurrent().getDesktop().getId());
//		System.out.println(Executions.getCurrent().);
//		String dtid = req.getParameter("tdtid");
		if (dtid != null) {
			 System.out.println(" use client dtid "+dtid);
		} else {
			System.out.println("dtid nulo");
		}
		
		
//		System.out.println("en nextDesktopId: " + dtid);
//		System.out.println("nextDesktopId: " + desktop);
		return dtid == null ? null : dtid;
//		
//		String cosa = String.valueOf(id);
//		System.out.println(id++);
//		Executions.getCurrent().setAttribute("dtid", cosa);
//		Executions.getCurrent().setResponseHeader("dtid_header", cosa);
//		Sessions.getCurrent().setAttribute("dtid_session", cosa);
//		
//		LiferayUtil.
//		return cosa;
	}

	public String nextPageUuid(Page page) {
//		System.out.println("nextPageUuid: " + page);
		return null;
	}

	@Override
	public String nextComponentUuid(Desktop desktop, Component arg1, ComponentInfo arg2) {
		String number;
		if ((number = (String) desktop.getAttribute("Id_Num")) == null) {
			number = "0";
			desktop.setAttribute("Id_Num", number);
		}
		int i = Integer.parseInt(number);
		i++;// Start from 1
		desktop.setAttribute("Id_Num", String.valueOf(i));
//		System.out.println("nextComponentUuid: " + "t_" + i);
		return "t_" + i;
	}
}
