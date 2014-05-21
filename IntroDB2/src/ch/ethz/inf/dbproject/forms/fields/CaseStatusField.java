package ch.ethz.inf.dbproject.forms.fields;


public class CaseStatusField extends SelectField {

	public CaseStatusField(String displayName) {
		super(displayName, "open", "Open Case", "closed", "Case Closed");
	}
}
