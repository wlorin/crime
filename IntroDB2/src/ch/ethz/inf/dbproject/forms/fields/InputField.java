package ch.ethz.inf.dbproject.forms.fields;

import java.util.HashMap;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;

import ch.ethz.inf.dbproject.forms.UserInputException;

public abstract class InputField<T> extends Field {

	private String type;
	private boolean allowNull;

	public InputField(String displayName, String type, boolean allowNull) {
		super(displayName);
		this.type = type;
		this.allowNull = allowNull;
	}

	@Override
	public String getHtmlCode(String value) {
		if (value == null) {
			value = "";
		}
		return String.format("<tr><th>%s</th><td><input type=%s name=%s value=\"%s\" /></td></tr>", displayName, type, displayName, StringEscapeUtils.escapeHtml4(value));
	}

	@Override
	public void validate(HashMap<String, String> values) {
		 
	}
	
	public abstract T parse(HashMap<String, String> values);
	
	protected void validateNull(String value) {
		if (!allowNull && isNull(value)) {
			throw new UserInputException("Field " + displayName + " is required!");
		}
	}
	
	public boolean isNull(String value) {
		return allowNull && StringUtils.isBlank(value);
	}

	protected String getValue(HashMap<String, String> values) {
		return values.get(displayName);
	}
}