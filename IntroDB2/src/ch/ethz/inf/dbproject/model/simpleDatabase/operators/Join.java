package ch.ethz.inf.dbproject.model.simpleDatabase.operators;

import java.util.ArrayList;
import java.util.List;

import ch.ethz.inf.dbproject.model.simpleDatabase.Tuple;

public abstract class Join extends Operator {

	private final String[] columnsLeft;
	private final String[] columnsRight;
	final List<Tuple> left;
	final List<Tuple> right;
	public Join(Operator left, Operator right, String columnsLeft, String columnsRight) {
		this(left, right, new String[]{columnsLeft}, new String[]{columnsRight});
	}
	public Join(Operator left, Operator right, String[] columnsLeft, String[] columnsRight) {
		assert(columnsLeft.length == columnsRight.length);
		this.left = new ArrayList<Tuple>();
		while (left.moveNext()) {
			this.left.add(left.current());
		}
		this.right= new ArrayList<Tuple>();
		while (right.moveNext()) {
			this.right.add(right.current());
		}
		this.columnsLeft = columnsLeft;
		this.columnsRight = columnsRight;
	}
}
