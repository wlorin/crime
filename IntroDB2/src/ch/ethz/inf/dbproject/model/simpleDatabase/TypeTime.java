package ch.ethz.inf.dbproject.model.simpleDatabase;

import java.sql.Time;
import java.text.SimpleDateFormat;

public class TypeTime extends Type<Time> {

	public TypeTime() {
		super(8, false);
	}

	@Override
	public Class<Time> getType() {
		return Time.class;
	}

	@Override
	public Time parse(String string) {
		return Time.valueOf(string);
	}

	@Override
	protected String tToString(Time time) {
		return new SimpleDateFormat("HH:mm:ss").format(time);
	}

}
