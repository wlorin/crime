package ch.ethz.inf.dbproject.forms.fields;

import java.util.HashMap;


public class StringField extends InputField<String> {

	public StringField(String displayName, boolean allowNull) {
		super(displayName, "text", allowNull);
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
