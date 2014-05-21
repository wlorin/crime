package ch.ethz.inf.dbproject.forms.fields;

import java.util.HashMap;


public class PasswordField extends InputField<String> {

	public PasswordField(String displayName) {
		super(displayName, "password", false);
	}

	@Override
	public String parse(HashMap<String, String> values) {
		return getValue(values);
	}
}
