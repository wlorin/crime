package ch.ethz.inf.dbproject.model.simpleDatabase.operators;

import ch.ethz.inf.dbproject.model.simpleDatabase.Tuple;
import ch.ethz.inf.dbproject.model.simpleDatabase.TupleSchema;

public class As extends Operator {

	private final Operator op;

	public As(TupleSchema schema, Operator op, String prefix) {
		super(schema.prefixed(prefix));
		this.op = op;
	}

	@Override
	public boolean moveNext() {
		if (op.moveNext()) {
			current = new Tuple(schema, op.current.values);
			return true;
		}
		
		return false;
	}

}
