package co.com.tecnocom.csj.view.pqr.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.PropsUtil;

public class FileUtil {
    private final static String PQR_HOME = PropsUtil.get(PropsKeys.LIFERAY_HOME) + "/data/pqr_files";

    static {
	File pqrHome = new File(PQR_HOME);
	if (!pqrHome.exists()) {
	    pqrHome.mkdirs();
	}
    }

    public static long getLiferayLimitFileSizeInMB() {
	long maxLiferaySize = Long.valueOf(PropsUtil.get(PropsKeys.UPLOAD_SERVLET_REQUEST_IMPL_MAX_SIZE));
	return maxLiferaySize / 1024 / 1024;
    }

    public static boolean isValidSize(int currentSize) {
	long maxLiferaySize = Long.valueOf(PropsUtil.get(PropsKeys.UPLOAD_SERVLET_REQUEST_IMPL_MAX_SIZE));

	if (currentSize > maxLiferaySize) {
	    return false;
	} else {
	    return true;
	}
    }

    public static File getFile(String filename) {
	File pqrHome = new File(PQR_HOME);

	if (pqrHome.exists() && pqrHome.isDirectory()) {
	    File file = new File(pqrHome, filename);
	    if (file.exists()) {
		return file;
	    }
	}

	return null;
    }

    public static void putFile(String filename, byte[] filedata) {
	File pqrHome = new File(PQR_HOME);

	if (pqrHome.exists() && pqrHome.isDirectory()) {
	    File fileToCreate = new File(pqrHome, filename);

	    if (!fileToCreate.exists()) {
		FileOutputStream stream = null;
		try {
		    fileToCreate.createNewFile();
		    stream = new FileOutputStream(fileToCreate);
		    stream.write(filedata);
		} catch (IOException e) {
		    e.printStackTrace();
		} finally {
		    if (stream != null) {
			try {
			    stream.close();
			} catch (IOException e) {
			}
		    }
		}
	    }
	}
    }
}
