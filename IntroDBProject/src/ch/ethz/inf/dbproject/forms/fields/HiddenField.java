package ch.ethz.inf.dbproject.forms.fields;

import org.apache.commons.lang3.StringEscapeUtils;

public class HiddenField extends Field {

	public HiddenField(String displayName) {
		super(displayName);
	}
	
	@Override
	public String getHtmlCode(String value) {
		if (value == null) {
			value = "";
		}
		return String.format("<input type=hidden name=%s value=\"%s\" />", displayName, displayName, StringEscapeUtils.escapeHtml4(value));
	}

}
