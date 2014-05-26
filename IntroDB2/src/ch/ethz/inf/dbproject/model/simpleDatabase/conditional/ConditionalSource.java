package ch.ethz.inf.dbproject.model.simpleDatabase.conditional;

import ch.ethz.inf.dbproject.model.simpleDatabase.Tuple;
import ch.ethz.inf.dbproject.model.simpleDatabase.Type;

public interface ConditionalSource {
	public String value(Tuple t);

	public Type<?> valueType(Tuple t);
}
