package ch.ethz.inf.dbproject.model.simpleDatabase;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class TypeDate extends Type<Date> {

	public TypeDate() {
		super(10, false);
	}

	@Override
	public Class<Date> getType() {
		return Date.class;
	}

	@Override
	public Date parse(String string) {
		try {
			return new Date(new SimpleDateFormat("yyyy-MM-dd").parse(string).getTime());
		} catch (ParseException e) {
			e.printStackTrace();
			throw new IllegalArgumentException("Could not parse " + string);
		}
	}

	@Override
	protected String tToString(Date t) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		return simpleDateFormat.format(t);
	}
}
