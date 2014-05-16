package ch.ethz.inf.dbproject.model.simpleDatabase.operators;

import java.util.List;

import ch.ethz.inf.dbproject.model.simpleDatabase.Tuple;
import ch.ethz.inf.dbproject.model.simpleDatabase.conditional.Condition;

public class LeftJoin extends Join {

	public LeftJoin(Operator leftOp, Operator rightOp, Condition condition) {
		super(leftOp, rightOp, condition);
	}

	@Override
	protected void handleUnprocessedLeft(List<Tuple> joined, List<Tuple> unprocessedLeft) {
		joined.addAll(unprocessedLeft);
	}

	@Override
	protected void handleUnprocessedRight(List<Tuple> joined, List<Tuple> unprocessedRight) {
		// Ignore
	}
}
