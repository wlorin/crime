package ch.ethz.inf.dbproject.model.simpleDatabase.conditional;

import ch.ethz.inf.dbproject.model.simpleDatabase.Tuple;
import ch.ethz.inf.dbproject.model.simpleDatabase.Type;

public class Value implements ConditionalSource {
	final private String value;
	private Type type;

	public Value(final String value, Type type) {
		this.value = value;
		this.type = type;
	}
	
	@Override
	public String value(Tuple t) {
		return value;
	}

	@Override
	public Type valueType(Tuple t) {
		return type;
	}
}
