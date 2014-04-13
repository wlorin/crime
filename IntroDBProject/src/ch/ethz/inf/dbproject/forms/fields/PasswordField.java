package ch.ethz.inf.dbproject.forms.fields;

import org.apache.commons.lang3.StringEscapeUtils;

public class PasswordField extends Field {

	public PasswordField(String displayName) {
		super(displayName);
	}
	
	public String getHtmlCode(String value) {
		if (value == null) {
			value = "";
		}
		
		return String.format("<tr><th>%s</th><td><input type=password name=%s value=\"%s\" /></td></tr>", displayName, displayName, StringEscapeUtils.escapeHtml4(value));
	}

}
