package ch.ethz.inf.dbproject.model.simpleDatabase.operators;

public class InnerJoin extends Join {

	public InnerJoin(Operator left, Operator right, String columnsLeft,
			String columnsRight) {
		super(left, right, columnsLeft, columnsRight);
		// TODO Auto-generated constructor stub
	}

	public InnerJoin(Operator left, Operator right, String[] columnsLeft,
			String[] columnsRight) {
		super(left, right, columnsLeft, columnsRight);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean moveNext() {
		// TODO Auto-generated method stub
		return false;
	}

}
