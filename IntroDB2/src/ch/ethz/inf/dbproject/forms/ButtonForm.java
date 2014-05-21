package ch.ethz.inf.dbproject.forms;

import ch.ethz.inf.dbproject.forms.fields.HiddenField;


public abstract class ButtonForm extends Form {
	
	public static final String PARAMETER_FIELD = "__paramField";
	
	public String generateForm(String caption, String action, String param) {
		StringBuilder html = new StringBuilder();
		appendFormHeader(action, html);
		html.append(new HiddenField(PARAMETER_FIELD, false).getHtmlCode(param));
		html.append(submitButton(caption));
		appendFormFooter(html);
		return html.toString();
	}

	@Override
	protected String getMethod() {
		return "POST";
	}
}
