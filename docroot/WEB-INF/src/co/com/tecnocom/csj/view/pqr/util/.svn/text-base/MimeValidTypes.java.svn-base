package co.com.tecnocom.csj.view.pqr.util;

public enum MimeValidTypes {
    IMAGE(new String[] { "image/jpeg", "image/gif", "image/png" }), TEXT(new String[] { "application/msword", "application/pdf",
	    "application/vnd.openxmlformats-officedocument.wordprocessingml.document" }), COMPRESS(new String[] { "application/zip", "multipart/x-zip",
	    "application/x-rar-compressed", "application/x-zip-compressed" });

    private String[] mimeTypes;

    private MimeValidTypes(String[] mimeTypes) {
	this.mimeTypes = mimeTypes;
    }

    public static boolean isValidMimeType(String mimeType) {
	for (MimeValidTypes mimeValidTypes : MimeValidTypes.values()) {
	    for (String mime : mimeValidTypes.mimeTypes) {
		if (mime.equalsIgnoreCase(mimeType)) {
		    return true;
		}
	    }
	}

	return false;
    }
}
