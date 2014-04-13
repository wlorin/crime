package ch.ethz.inf.dbproject.forms.fields;

import java.util.HashMap;

import org.apache.commons.lang3.StringEscapeUtils;


public class TextArea extends Field {

	public TextArea(String displayName) {
		super(displayName);
	}

	@Override
	public String getHtmlCode(String value) {
		if (value == null) {
			value = "";
		}
		return String.format("<tr><th>%s</th><td><textarea name=%s >%s</textarea></td></tr>", displayName, displayName, StringEscapeUtils.escapeHtml4(value));
	}

	@Override
	public void validate(HashMap<String, String> values) {
		// Always succeeds
	}
	
	public String getValue(HashMap<String, String> values) {
		return values.get(displayName);
	}
}
