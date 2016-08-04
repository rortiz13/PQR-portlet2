package co.com.tecnocom.csj.view.pqr.admin;

import java.util.LinkedList;
import java.util.List;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WrongValueException;

import co.com.tecnocom.csj.core.util.PQRDataUtil;
import co.com.tecnocom.csj.core.util.dto.PQR;
import co.com.tecnocom.csj.core.util.dto.PQRType;
import co.com.tecnocom.csj.view.pqr.util.LiferayUtil;
import co.com.tecnocom.csj.view.pqr.util.LiferayUtil.ROLE;
import co.com.tecnocom.csj.view.pqr.util.ListUtil;

public class PQRAdmin {

	private ROLE userRole;

	private List<PQR> allPqrs;
	private List<PQR> pqrs;

	private List<PQRType> validTypes;
	private PQRType selectedType;

	private String pqrId;

	private boolean popupDetailVisible;

	public PQRAdmin() {
		userRole = LiferayUtil.getAdminRole();

		validTypes = new LinkedList<PQRType>(PQRDataUtil.INSTANCE.getPQRTypes());
		PQRType allTypes = new PQRType();
		allTypes.setId(-1);
		allTypes.setName("Todos");
		validTypes.add(0, allTypes);
		selectedType = allTypes;

		if (isAuthorized()) {
			refreshPQRs();
		}
	}

	private void refreshPQRs() {
		if (ROLE.PQR_ADMIN.equals(userRole)) {
			allPqrs = PQRDataUtil.INSTANCE.getPQRs(null, LiferayUtil.getCurrentScopeGroupId());
		} else {
			allPqrs = PQRDataUtil.INSTANCE.getPQRs(LiferayUtil.getLoggedLiferayUserId(), LiferayUtil.getCurrentScopeGroupId());
		}

		filterPQRList();
	}

	@Command
	@NotifyChange({ "pqrs", "selectedType" })
	public void searchPQRId(@BindingParam("component") Component component) {
		try {
			pqrs = ListUtil.applyFilter(allPqrs, pqrId);
			selectedType = validTypes.get(0);
		} catch (NumberFormatException e) {
			throw new WrongValueException(component, Labels.getLabel("search_input_empty"));
		}
	}

	@Command
	@NotifyChange({ "pqrs", "pqrId" })
	public void filterPQRList() {
		pqrId = null;
		pqrs = ListUtil.applyFilter(allPqrs, selectedType);
	}

	@Command
	@NotifyChange("popupDetailVisible")
	public void openPopup() {
		popupDetailVisible = true;
	}

	@GlobalCommand
	@NotifyChange({ "popupDetailVisible", "pqrs" })
	public void closePopup() {
		popupDetailVisible = false;
		refreshPQRs();
	}

	public boolean isAuthorized() {
		return userRole != null;
	}

	public List<PQR> getPqrs() {
		return pqrs;
	}

	public void setPqrs(List<PQR> pqrs) {
		this.pqrs = pqrs;
	}

	public boolean isPopupDetailVisible() {
		return popupDetailVisible;
	}

	public void setPopupDetailVisible(boolean popupDetailVisible) {
		this.popupDetailVisible = popupDetailVisible;
	}

	public List<PQRType> getValidTypes() {
		return validTypes;
	}

	public void setValidTypes(List<PQRType> validTypes) {
		this.validTypes = validTypes;
	}

	public PQRType getSelectedType() {
		return selectedType;
	}

	public void setSelectedType(PQRType selectedType) {
		this.selectedType = selectedType;
	}

	public String getPqrId() {
		return pqrId;
	}

	public void setPqrId(String pqrId) {
		this.pqrId = pqrId;
	}
}
