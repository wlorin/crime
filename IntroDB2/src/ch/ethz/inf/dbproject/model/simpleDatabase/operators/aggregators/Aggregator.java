package ch.ethz.inf.dbproject.model.simpleDatabase.operators.aggregators;

import ch.ethz.inf.dbproject.model.simpleDatabase.Type;

public abstract class Aggregator<T> {
	public abstract T init(T t);
	public abstract T aggregate(T aggregate, T x);
	public abstract Type<T> getSchemaType();
}
