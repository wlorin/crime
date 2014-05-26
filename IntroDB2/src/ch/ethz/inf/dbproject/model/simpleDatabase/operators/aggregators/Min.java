package ch.ethz.inf.dbproject.model.simpleDatabase.operators.aggregators;

import ch.ethz.inf.dbproject.model.simpleDatabase.Type;
import ch.ethz.inf.dbproject.model.simpleDatabase.TypeInt;

public class Min extends Aggregator<Integer> {

	@Override
	public Integer aggregate(Integer aggregate, Integer x) {
		return Math.min(aggregate, x);
	}

	@Override
	public Type<Integer> getSchemaType() {
		return new TypeInt();
	}

	@Override
	public Integer init(Integer t) {
		return t;
	}
}
