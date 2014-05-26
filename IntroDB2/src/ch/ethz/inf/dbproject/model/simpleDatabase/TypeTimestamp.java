package ch.ethz.inf.dbproject.model.simpleDatabase;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class TypeTimestamp extends Type<Timestamp> {

	public TypeTimestamp() {
		super(19, false);
	}

	public TypeTimestamp(boolean isPrimary) {
		super(19, false, isPrimary);
	}

	@Override
	public Class<Timestamp> getType() {
		return Timestamp.class;
	}

	@Override
	public Timestamp parse(String string) {
		return Timestamp.valueOf(string);
	}

	@Override
	protected String tToString(Timestamp t) {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(t);
	}

}
