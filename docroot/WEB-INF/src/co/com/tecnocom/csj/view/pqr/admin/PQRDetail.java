package co.com.tecnocom.csj.view.pqr.admin;

import java.io.FileNotFoundException;
import java.util.List;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zul.Button;
import org.zkoss.zul.Filedownload;

import co.com.tecnocom.csj.core.util.AuditUtil;
import co.com.tecnocom.csj.core.util.PQRDataUtil;
import co.com.tecnocom.csj.core.util.dto.PQR;
import co.com.tecnocom.csj.core.util.dto.PQRRecord;
import co.com.tecnocom.csj.core.util.dto.PQRStatus;
import co.com.tecnocom.csj.view.pqr.util.FileUtil;
import co.com.tecnocom.csj.view.pqr.util.LiferayUtil;
import co.com.tecnocom.csj.view.pqr.util.ValidatorUtil;

import com.liferay.portal.model.User;
import com.liferay.portal.service.ServiceContextThreadLocal;

public class PQRDetail {
	private ValidatorUtil validator = new ValidatorUtil();

	private PQR selectedPQR;
	private boolean editable;

	private List<User> validResponsibleUsers;
	private User selectedUser;

	private List<PQRStatus> validStatuses;
	private PQRStatus selectedStatus;

	private String newComment;

	public PQRDetail() {
		validResponsibleUsers = LiferayUtil.getValidPQRUsers();
		validStatuses = PQRDataUtil.INSTANCE.getPQRStatuses();
	}

	@GlobalCommand
	@NotifyChange({ "selectedPQR", "selectedStatus", "selectedUser", "newComment", "editable" })
	public void loadPopupData(@BindingParam("selectedPQR") PQR selectedPQR) {
		newComment = null;
		selectedUser = null;
		selectedStatus = null;

		this.selectedPQR = selectedPQR;

		editable = PQRDataUtil.INSTANCE.isFinalStatus(selectedPQR.getCurrentStatus());

		selectedStatus = PQRDataUtil.INSTANCE.getPQRStatusByName(selectedPQR.getCurrentStatus());

		if (selectedPQR.getLiferayUserId() != null && !selectedPQR.getLiferayUserId().equals(0L)) {
			selectedUser = LiferayUtil.getUserById(validResponsibleUsers, selectedPQR.getLiferayUserId());
			if (selectedUser != null) {
				this.selectedPQR.setLiferayUserName(selectedUser.getFullName());
			}
		} else {
			this.selectedPQR.setLiferayUserId(LiferayUtil.getLoggedLiferayUserId());
			this.selectedPQR.setLiferayUserName(LiferayUtil.getLiferayUserName(this.selectedPQR.getLiferayUserId()));
		}

		if (this.selectedPQR.getRecords() != null) {
			for (PQRRecord record : this.selectedPQR.getRecords()) {
				record.setLiferayUserName(LiferayUtil.getLiferayUserNameWithOccupationAndDependency(record.getLiferayUserId()));
			}
		}
	}

	@Command
	public void update(@BindingParam("component") Button button) {
		if (selectedUser == null) {
			throw new WrongValueException(button, Labels.getLabel("no_responsible"));
		}
		if (newComment == null || newComment.trim().isEmpty()) {
			throw new WrongValueException(button, Labels.getLabel("no_comment"));
		}

		PQRDataUtil.INSTANCE.updatePQRStatus(selectedStatus.getId(), selectedPQR.getId(), newComment, selectedUser.getUserId(), selectedPQR.getLiferayUserId());
		AuditUtil.INSTANCE.auditResource(ServiceContextThreadLocal.getServiceContext(), LiferayUtil.getLayoutURL(), Long.valueOf(selectedPQR.getId()).intValue(), "Actualizar PQR", "tecnocom_pqr");
	}

	@Command
	public void getFile() {
		try {
			Filedownload.save(FileUtil.getFile(selectedPQR.getFilename()), null);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public PQR getSelectedPQR() {
		return selectedPQR;
	}

	public void setSelectedPQR(PQR selectedPQR) {
		this.selectedPQR = selectedPQR;
	}

	public List<User> getValidResponsibleUsers() {
		return validResponsibleUsers;
	}

	public void setValidResponsibleUsers(List<User> validResponsibleUsers) {
		this.validResponsibleUsers = validResponsibleUsers;
	}

	public User getSelectedUser() {
		return selectedUser;
	}

	public void setSelectedUser(User selectedUser) {
		this.selectedUser = selectedUser;
	}

	public List<PQRStatus> getValidStatuses() {
		return validStatuses;
	}

	public void setValidStatuses(List<PQRStatus> validStatuses) {
		this.validStatuses = validStatuses;
	}

	public PQRStatus getSelectedStatus() {
		return selectedStatus;
	}

	public void setSelectedStatus(PQRStatus selectedStatus) {
		this.selectedStatus = selectedStatus;
	}

	public String getNewComment() {
		return newComment;
	}

	public void setNewComment(String newComment) {
		this.newComment = newComment;
	}

	public boolean isEditable() {
		return editable;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
	}

	public ValidatorUtil getValidator() {
		return validator;
	}
}