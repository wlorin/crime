package ch.ethz.inf.dbproject.model.simpleDatabase.conditional;

import ch.ethz.inf.dbproject.model.simpleDatabase.Tuple;

public interface Condition {
	public boolean matches(Tuple t);
}
