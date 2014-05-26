package ch.ethz.inf.dbproject.model.simpleDatabase.operators.aggregators;

import ch.ethz.inf.dbproject.model.simpleDatabase.Type;
import ch.ethz.inf.dbproject.model.simpleDatabase.TypeInt;

public class Count extends Aggregator<Integer> {

	@Override
	public Integer aggregate(Integer aggregate, Integer x) {
		return aggregate + 1;
	}

	@Override
	public Type<Integer> getSchemaType() {
		return new TypeInt();
	}

	@Override
	public Integer init(Integer t) {
		return 1;
	}
}
