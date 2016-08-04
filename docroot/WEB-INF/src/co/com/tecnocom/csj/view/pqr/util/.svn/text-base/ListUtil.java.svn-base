package co.com.tecnocom.csj.view.pqr.util;

import java.util.LinkedList;
import java.util.List;

import co.com.tecnocom.csj.core.util.dto.PQR;
import co.com.tecnocom.csj.core.util.dto.PQRType;

public class ListUtil {

    public static List<PQR> applyFilter(List<PQR> allPQR, PQRType pqrType) {
	if (pqrType != null && pqrType.getId() == -1) {
	    return new LinkedList<PQR>(allPQR);
	}

	List<PQR> filtered = new LinkedList<PQR>();

	if (allPQR != null) {
	    for (PQR pqr : allPQR) {
		if (pqr.getPqrType().equalsIgnoreCase(pqrType.getName())) {
		    filtered.add(pqr);
		}
	    }
	}

	return filtered;
    }

    public static List<PQR> applyFilter(List<PQR> allPQR, String pqrId) {
	if (pqrId == null || pqrId.trim().isEmpty()) {
	    return new LinkedList<PQR>(allPQR);
	}

	List<PQR> filtered = new LinkedList<PQR>();

	if (allPQR != null) {
	    for (PQR pqr : allPQR) {
		if (pqr.getId() == Long.valueOf(pqrId).longValue()) {
		    filtered.add(pqr);
		    break;
		}
	    }
	}

	return filtered;
    }

}
