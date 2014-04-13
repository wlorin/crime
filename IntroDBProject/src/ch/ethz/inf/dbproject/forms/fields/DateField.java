package ch.ethz.inf.dbproject.forms.fields;

import java.sql.Date;
import java.util.HashMap;

import ch.ethz.inf.dbproject.forms.UserInputException;


public class DateField extends InputField<Date> {

	public DateField(String displayName, boolean allowNull) {
		super(displayName, "date", allowNull);
	}

	@Override
	public void validate(HashMap<String, String> values) {
		String value = getValue(values);
		try {
			parse(values);
		}
		catch (IllegalArgumentException e) {
			throw new UserInputException(value + " is not a valid date");
		}
	}

	@Override
	public Date parse(HashMap<String, String> values) {
		String value = getValue(values);
		if (isNull(value)) {
			return null;
		}
		else {
			return Date.valueOf(value);
		}
	}
}
