package ch.ethz.inf.dbproject.forms.fields;

import java.util.HashMap;


public class HiddenField extends InputField<String> {

	public HiddenField(String displayName, boolean allowNull) {
		super(displayName, "hidden", allowNull);
	}

	@Override
	public String parse(HashMap<String, String> values) {
		String value = getValue(values);
		if (isNull(value)) {
			return null;
		}
		return value;
	}
}
