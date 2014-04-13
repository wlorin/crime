package ch.ethz.inf.dbproject.forms.fields;

import org.apache.commons.lang3.StringEscapeUtils;

public class StringField extends Field {

	public StringField(String displayName) {
		super(displayName);
	}
	
	@Override
	public String getHtmlCode(String value) {
		if (value == null) {
			value = "";
		}
		return String.format("<tr><th>%s</th><td><input type=text name=%s value=\"%s\" /></td></tr>", displayName, displayName, StringEscapeUtils.escapeHtml4(value));
	}

}
