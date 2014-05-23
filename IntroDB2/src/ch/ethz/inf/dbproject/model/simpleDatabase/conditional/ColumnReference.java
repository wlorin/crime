package ch.ethz.inf.dbproject.model.simpleDatabase.conditional;

import ch.ethz.inf.dbproject.model.simpleDatabase.Tuple;
import ch.ethz.inf.dbproject.model.simpleDatabase.Type;

public class ColumnReference implements ConditionalSource {
	final private String name;

	public ColumnReference(final String name) {
		this.name = name;
	}
	
	@Override
	public String value(Tuple t) {
		return t.getString(name);
	}

	@Override
	public Type valueType(Tuple t) {
		return t.getType(name);
	}
}
