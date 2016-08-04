package co.com.tecnocom.csj.view.pqr.user;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.zkforge.bwcaptcha.Captcha;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.util.resource.Labels;
import org.zkoss.web.portlet.RenderHttpServletRequest;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zul.Button;
import org.zkoss.zul.Textbox;

import co.com.tecnocom.csj.core.util.AuditUtil;
import co.com.tecnocom.csj.core.util.CommonDataUtil;
import co.com.tecnocom.csj.core.util.EmailUtil;
import co.com.tecnocom.csj.core.util.PQRDataUtil;
import co.com.tecnocom.csj.core.util.dto.City;
import co.com.tecnocom.csj.core.util.dto.Department;
import co.com.tecnocom.csj.core.util.dto.DocumentType;
import co.com.tecnocom.csj.core.util.dto.PQRType;
import co.com.tecnocom.csj.core.util.properties.DatasourceProperties;
import co.com.tecnocom.csj.view.pqr.portlet.PQRPortlet;
import co.com.tecnocom.csj.view.pqr.util.DateUtil;
import co.com.tecnocom.csj.view.pqr.util.FileUtil;
import co.com.tecnocom.csj.view.pqr.util.LiferayUtil;
import co.com.tecnocom.csj.view.pqr.util.MimeValidTypes;
import co.com.tecnocom.csj.view.pqr.util.ValidatorUtil;

import com.liferay.portal.service.ServiceContextThreadLocal;

public class PQR {
	private boolean formVisible;
	private boolean portletConfigured;

	private ValidatorUtil validator = new ValidatorUtil();

	private List<DocumentType> documentTypes;
	private List<Department> departments;
	private List<City> cities;
	private List<PQRType> pqrTypes;

	private DocumentType selectedDocumentType;
	private Department selectedDepartment;
	private City selectedCity;
	private PQRType selectedPQRType;
	private String selectedFileName;
	private byte[] selectedFileData;

	private String id;
	private String name;
	private String phone;
	private String email;
	private String address;
	private String comment;

	private String successMessage;

	private String email_from, email_subject;

	public PQR() {
		email_from = ((RenderHttpServletRequest) Executions.getCurrent().getNativeRequest()).getRenderRequest().getPreferences().getValue(PQRPortlet.PQR_EMAIL_PREFERENCE, null);
		email_subject = ((RenderHttpServletRequest) Executions.getCurrent().getNativeRequest()).getRenderRequest().getPreferences().getValue(PQRPortlet.PQR_SUBJECT_PREFERENCE, null);

		portletConfigured = (email_from == null || email_from.isEmpty()) || (email_subject == null || email_subject.isEmpty()) ? false : true;

		formVisible = true;

		documentTypes = CommonDataUtil.INSTANCE.getDocumentTypes(DatasourceProperties.INSTANCE.getPQRDS());
		departments = new ArrayList<Department>(CommonDataUtil.INSTANCE.getDepartmentsAndCities(DatasourceProperties.INSTANCE.getPQRDS()).values());
		pqrTypes = new ArrayList<PQRType>(PQRDataUtil.INSTANCE.getPQRTypes());
	}

	@Command
	@NotifyChange("cities")
	public void onChangeDepartment() {
		cities = new ArrayList<City>(selectedDepartment.getCities().values());
	}

	@Command
	public void changeCaptcha(@BindingParam("captcha") Captcha captcha) {
		captcha.randomValue();
	}

	@Command
	@NotifyChange("selectedFileName")
	public void upload(@BindingParam("fileUploadEvent") UploadEvent event, @BindingParam("component") Button button, @BindingParam("submitButton") Button submitButton) {
		submitButton.setDisabled(false);

		String contentType = event.getMedia().getContentType();

		if (!MimeValidTypes.isValidMimeType(contentType)) {
			throw new WrongValueException(button, Labels.getLabel("invalid_content_type"));
		}

		selectedFileName = event.getMedia().getName();

		if (event.getMedia().isBinary()) {
			selectedFileData = event.getMedia().getByteData();
		} else {
			try {
				selectedFileData = event.getMedia().getStringData().getBytes("UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}

		if (!FileUtil.isValidSize(selectedFileData.length)) {
			selectedFileData = null;
			selectedFileName = null;
			throw new WrongValueException(button, Labels.getLabel("invalid_file_size", new Object[] { FileUtil.getLiferayLimitFileSizeInMB() }));
		}
	}

	@Command
	@NotifyChange({ "formVisible", "successMessage" })
	public void send(@BindingParam("captchaTextBox") Textbox textbox, @BindingParam("captcha") Captcha captcha) {
		verifyCaptchaIgnoreCase(textbox, captcha);

		if (selectedFileName != null && !selectedFileName.isEmpty() && selectedFileData != null) {
			selectedFileName = DateUtil.generateFileName(selectedFileName);
			FileUtil.putFile(selectedFileName, selectedFileData);
		}

		Long key = PQRDataUtil.INSTANCE.insertPQR(selectedDocumentType.getId(), id, name, phone, email, address, selectedCity.getId(), selectedPQRType.getId(), comment, selectedFileName, LiferayUtil.getCurrentScopeGroupId());
		AuditUtil.INSTANCE.auditResource(ServiceContextThreadLocal.getServiceContext(), LiferayUtil.getLayoutURL(), key.intValue(), "Crear PQR", "tecnocom_pqr");
		
		formVisible = false;

		EmailUtil.INSTANCE.sendEmail(email_from, email, email_subject, Executions.getCurrent().getDesktop().getWebApp().getServletContext(), PQRPortlet.PQR_EMAIL_HTML_LOCATION, String.valueOf(key));

		successMessage = Labels.getLabel("pqr_success", new Object[] { key.toString() });
	}

	private void verifyCaptchaIgnoreCase(Textbox tbox, Captcha capt) {
		if (!capt.getValue().equalsIgnoreCase(tbox.getValue())) {
			throw new WrongValueException(tbox, Labels.getLabel("captcha_error"));
		}
	}

	public List<DocumentType> getDocumentTypes() {
		return documentTypes;
	}

	public void setDocumentTypes(List<DocumentType> documentTypes) {
		this.documentTypes = documentTypes;
	}

	public DocumentType getSelectedDocumentType() {
		return selectedDocumentType;
	}

	public void setSelectedDocumentType(DocumentType selectedDocumentType) {
		this.selectedDocumentType = selectedDocumentType;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public List<Department> getDepartments() {
		return departments;
	}

	public void setDepartments(List<Department> departments) {
		this.departments = departments;
	}

	public List<City> getCities() {
		return cities;
	}

	public void setCities(List<City> cities) {
		this.cities = cities;
	}

	public Department getSelectedDepartment() {
		return selectedDepartment;
	}

	public void setSelectedDepartment(Department selectedDepartment) {
		this.selectedDepartment = selectedDepartment;
	}

	public City getSelectedCity() {
		return selectedCity;
	}

	public void setSelectedCity(City selectedCity) {
		this.selectedCity = selectedCity;
	}

	public List<PQRType> getPqrTypes() {
		return pqrTypes;
	}

	public void setPqrTypes(List<PQRType> pqrTypes) {
		this.pqrTypes = pqrTypes;
	}

	public PQRType getSelectedPQRType() {
		return selectedPQRType;
	}

	public void setSelectedPQRType(PQRType selectedPQRType) {
		this.selectedPQRType = selectedPQRType;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public ValidatorUtil getValidator() {
		return validator;
	}

	public String getSelectedFileName() {
		return selectedFileName;
	}

	public void setSelectedFileName(String selectedFileName) {
		this.selectedFileName = selectedFileName;
	}

	public boolean isFormVisible() {
		return formVisible;
	}

	public void setFormVisible(boolean formVisible) {
		this.formVisible = formVisible;
	}

	public boolean isPortletConfigured() {
		return portletConfigured;
	}

	public void setPortletConfigured(boolean portletConfigured) {
		this.portletConfigured = portletConfigured;
	}

	public String getSuccessMessage() {
		return successMessage;
	}

	public void setSuccessMessage(String successMessage) {
		this.successMessage = successMessage;
	}
}
