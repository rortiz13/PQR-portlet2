package co.com.tecnocom.csj.view.pqr.util;

import java.util.LinkedList;
import java.util.List;

import org.zkoss.zk.ui.Executions;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.model.Role;
import com.liferay.portal.model.User;
import com.liferay.portal.service.RoleLocalServiceUtil;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.PortalUtil;

public class LiferayUtil {
	public enum ROLE {
		PQR_ADMIN("Administrador de PQR"), PQR_RESPONSIBLE("Responsable de PQR");

		private String role;

		private ROLE(String role) {
			this.role = role;
		}

		public String getRole() {
			return role;
		}

		public static ROLE getRoleEnum(List<Role> userRoles) {
			for (ROLE role : values()) {
				for (Role userRole : userRoles) {
					if (role.getRole().equalsIgnoreCase(userRole.getName())) {
						return role;
					}
				}
			}

			return null;
		}
	}

	public static User getUserById(List<User> users, Long id) {
		if (users != null && id != null) {
			for (User user : users) {
				if (id.equals(user.getUserId())) {
					return user;
				}
			}
		}

		return null;
	}

	public static List<User> getValidPQRUsers() {
		try {
			List<User> validUsers = new LinkedList<User>();

			List<User> users = UserLocalServiceUtil.getRoleUsers(RoleLocalServiceUtil.getRole(getCurrentThemeDisplay().getCompanyId(), ROLE.PQR_RESPONSIBLE.role).getRoleId());

			for (User user : users) {
				if (user.isActive()) {
					validUsers.add(user);
				}
			}

			return validUsers;
		} catch (SystemException e) {
			e.printStackTrace();
		} catch (PortalException e) {
			e.printStackTrace();
		}

		return new LinkedList<User>();
	}

	public static String getLiferayUserName(Long id) {
		if (id == null) {
			return "";
		}

		try {
			return UserLocalServiceUtil.getUser(id).getFullName();
		} catch (PortalException e) {
			e.printStackTrace();
		} catch (SystemException e) {
			e.printStackTrace();
		}

		return "";
	}
	
	public static String getLiferayUserNameWithOccupationAndDependency(Long id) {
		if (id == null) {
			return "";
		}

		try {
			User pqrRecordUser = UserLocalServiceUtil.getUser(id);
			String userOccupation = "", userDependency = "";
			if(pqrRecordUser.getExpandoBridge().hasAttribute("cargo")) {
				userOccupation = pqrRecordUser.getExpandoBridge().getAttribute("cargo").toString();
			}
			if(pqrRecordUser.getExpandoBridge().hasAttribute("dependencia")) {
				userDependency = pqrRecordUser.getExpandoBridge().getAttribute("dependencia").toString();
			}
			
			StringBuilder fullNameToShow = new StringBuilder(pqrRecordUser.getFullName());
			System.out.println("name: " + fullNameToShow);
			System.out.println(userOccupation);
			System.out.println(userDependency);
			
			if(userOccupation != null && !userOccupation.isEmpty()) {
				fullNameToShow.append(" (").append(userOccupation).append(")");
			}
			if(userDependency != null && !userDependency.isEmpty()) {
				fullNameToShow.append(" - ").append(userDependency).append("");
			}
			
			return fullNameToShow.toString();
		} catch (PortalException e) {
			e.printStackTrace();
		} catch (SystemException e) {
			e.printStackTrace();
		}

		return "";
	}

	public static long getLoggedLiferayUserId() {
		return getCurrentThemeDisplay().getUserId();
	}

	public static ROLE getAdminRole() {
		try {
			ThemeDisplay themeDisplay = getCurrentThemeDisplay();
			List<Role> userRoles = themeDisplay.getUser().getRoles();

			if (userRoles != null) {
				return ROLE.getRoleEnum(userRoles);
			}
		} catch (SystemException e) {
			e.printStackTrace();
		}

		return null;
	}

	private static ThemeDisplay getCurrentThemeDisplay() {
		return (ThemeDisplay) Executions.getCurrent().getAttribute(WebKeys.THEME_DISPLAY);
	}

	public static long getCurrentScopeGroupId() {
		return getCurrentThemeDisplay().getScopeGroupId();
	}

	public static String getLayoutURL() {
		try {
			return PortalUtil.getLayoutURL(getCurrentThemeDisplay());
		} catch (PortalException e) {
			e.printStackTrace();
		} catch (SystemException e) {
			e.printStackTrace();
		}

		return "";
	}
}
