package ch.ethz.inf.dbproject.forms.fields;

import java.util.HashMap;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;

import ch.ethz.inf.dbproject.forms.UserInputException;

public class SelectField extends Field {

	private String[] options;

	public SelectField(String displayName, String... optionsValue__Display) {
		super(displayName);
		if (optionsValue__Display.length % 2 != 0) {
			throw new IllegalArgumentException("options must be paired, even # of arguments expected");
		}
		this.options = optionsValue__Display;
	}
	
	@Override
	public String getHtmlCode(String value) {
		if (value == null) {
			value = "";
		}
		StringBuilder sbOptions = new StringBuilder();
		for (int i = 0; i < options.length; i += 2) {
			
			String i_value = options[i];
			sbOptions.append(
				String.format("<option %s value=\"%s\">%s</option>",
				              value.equals(i_value) ? "selected=selected" : "",
				              StringEscapeUtils.escapeHtml4(i_value), 
				              StringEscapeUtils.escapeHtml4(options[i + 1]
			)));
		}
		return String.format("<tr><th>%s</th><td><select type=text name=%s>%s</select></td></tr>", displayName, displayName, sbOptions.toString(), StringEscapeUtils.escapeHtml4(value));
	}

	@Override
	public void validate(HashMap<String, String> values) {
		String option = values.get(displayName);
		
		if(!ArrayUtils.contains(options, option)) {
			throw new UserInputException(option + " is not a valid option: " + StringUtils.join(options, ", "));
		}
	}

}
