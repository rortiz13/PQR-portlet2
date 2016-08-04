package co.com.tecnocom.csj.view.pqr.user;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WrongValueException;

import co.com.tecnocom.csj.core.util.PQRDataUtil;
import co.com.tecnocom.csj.core.util.dto.PQRRecord;

public class PQRFinder {
    private String pqrId;

    private boolean searched;

    private PQRRecord pqr;

    @Command
    @NotifyChange({ "pqr", "searched", "finalState" })
    public void findPQR(@BindingParam("component") Component component) {
	if (pqrId == null || pqrId.trim().isEmpty()) {
	    throw new WrongValueException(component, Labels.getLabel("search_input_empty"));
	}

	try {
	    long id = Long.valueOf(pqrId);

	    searched = true;

	    pqr = PQRDataUtil.INSTANCE.getLastPQRStatus(id);

	} catch (NumberFormatException e) {
	    throw new WrongValueException(component, Labels.getLabel("search_input_empty"));
	}
    }

    public String getPqrId() {
	return pqrId;
    }

    public void setPqrId(String pqrId) {
	this.pqrId = pqrId;
    }

    public PQRRecord getPqr() {
	return pqr;
    }

    public void setPqr(PQRRecord pqr) {
	this.pqr = pqr;
    }

    public boolean isSearched() {
	return searched;
    }

    public void setSearched(boolean searched) {
	this.searched = searched;
    }

    public boolean isFinalState() {
	if (pqr != null) {
	    return PQRDataUtil.INSTANCE.isFinalStatus(pqr.getStatusName());
	}

	return false;
    }
}
