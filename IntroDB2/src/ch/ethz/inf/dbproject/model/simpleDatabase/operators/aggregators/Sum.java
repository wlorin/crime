package ch.ethz.inf.dbproject.model.simpleDatabase.operators.aggregators;

import ch.ethz.inf.dbproject.model.simpleDatabase.Type;
import ch.ethz.inf.dbproject.model.simpleDatabase.TypeInt;

public class Sum extends Aggregator<Integer> {

	@Override
	public Integer aggregate(Integer aggregate, Integer x) {
		return aggregate + x;
	}

	@Override
	public Type<Integer> getSchemaType() {
		return new TypeInt();
	}
}
