package co.com.tecnocom.csj.view.pqr.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.Validator;
import org.zkoss.bind.validator.AbstractValidator;
import org.zkoss.util.resource.Labels;

public class ValidatorUtil {

    public Validator getRequiredValidator() {
	return new AbstractValidator() {

	    @Override
	    public void validate(ValidationContext ctx) {
		if (isPropertyValueEmpty(ctx.getProperty().getValue())) {
		    addInvalidMessage(ctx, Labels.getLabel("required_field"));
		}
	    }
	};
    }
    
    public Validator getRequiredPQRDetailValidator() {
	return new AbstractValidator() {

	    @Override
	    public void validate(ValidationContext ctx) {
		if (isPropertyValueEmpty(ctx.getProperty().getValue())) {
		    addInvalidMessage(ctx, Labels.getLabel("pqr_detail_required_field"));
		}
	    }
	};
    }

    public Validator getEmailValidator() {
	return new AbstractValidator() {

	    @Override
	    public void validate(ValidationContext ctx) {
		String email = ctx.getProperty().getValue() == null ? "" : ctx.getProperty().getValue().toString();

		Pattern pattern = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
		Matcher matcher = pattern.matcher(email);

		if (!matcher.matches()) {
		    addInvalidMessage(ctx, Labels.getLabel("invalid_email"));
		}
	    }
	};
    }

    private boolean isPropertyValueEmpty(Object value) {
	if (value == null || (value instanceof String && (((String) value).isEmpty() || ((String) value).trim().length() == 0))) {
	    return true;
	}
	return false;
    }
}
