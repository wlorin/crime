package ch.ethz.inf.dbproject.forms.fields;

public abstract class Field {
	protected String displayName;

	public String getDisplayName() {
		return displayName;
	}

	public Field(String displayName) {
		this.displayName = displayName;
	}

	public abstract String getHtmlCode(String value);
}
