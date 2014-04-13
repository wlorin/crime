package ch.ethz.inf.dbproject.forms.fields;

import java.util.HashMap;

import org.apache.commons.lang3.StringEscapeUtils;


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
	
	@Override
	public String getHtmlCode(String value) {
		if (value == null) {
			value = "";
		}
		return String.format("<input type=hidden name=%s value=\"%s\" />", displayName, StringEscapeUtils.escapeHtml4(value));
	}
}
