package co.com.tecnocom.csj.view.pqr.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
    public static String generateFileName(String currentFilename) {
	DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
	String newFilename = dateFormat.format(new Date());

	return newFilename + currentFilename.substring(currentFilename.lastIndexOf("."));
    }
}
