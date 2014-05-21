package ch.ethz.inf.dbproject.forms.fields;

import java.sql.Time;
import java.util.HashMap;

import ch.ethz.inf.dbproject.forms.UserInputException;


public class TimeField extends InputField<Time> {

	public TimeField(String displayName, boolean allowNull) {
		super(displayName, "time", allowNull);
	}

	@Override
	public void validate(HashMap<String, String> values) {
		String value = getValue(values);
		validateNull(value);
		try {
			parse(values);
		}
		catch (IllegalArgumentException e) {
			throw new UserInputException(value + " is not a valid time");
		}
	}

	public Time parse(HashMap<String, String> values) {
		String value = getValue(values);
		if (isNull(value)) {
			return null;
		}
		return Time.valueOf(value + ":00");
	}
}
