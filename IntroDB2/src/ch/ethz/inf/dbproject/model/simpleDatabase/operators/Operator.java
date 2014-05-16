package ch.ethz.inf.dbproject.model.simpleDatabase.operators;

import ch.ethz.inf.dbproject.model.simpleDatabase.Tuple;
import ch.ethz.inf.dbproject.model.simpleDatabase.TupleSchema;
import ch.ethz.inf.dbproject.model.simpleDatabase.conditional.Condition;

/**
 * Base class of all operators. An operator processes one tuple at a time. It
 * allows an application to call moveNext() to move to the next tuple. After
 * moveNext() the application can retrieve the new tuple by a call to current().
 */
public abstract class Operator {
	final public TupleSchema schema;
	
	public Operator(TupleSchema schema) {
		this.schema = schema;
	}

	/**
	 * The current tuple.
	 */
	protected Tuple current;

	/**
	 * Moves forward to the next tuple. The next tuple can be retrieved by a
	 * call to current(). If there is no more tuple, this method returns false.
	 * @return true, if we advanced to next tuple
	 */
	public abstract boolean moveNext();

	/**
	 * @return the current tuple
	 */
	public final Tuple current() {
		return this.current;
	}
	
	
	public final Operator as(String name) {
		return new As(this, name);
	}
	
	public final InnerJoin join(Operator rightOp, Condition condition) {
		return new InnerJoin(this, rightOp, condition);
	}
	
	public final LeftJoin joinLeft(Operator rightOp, Condition condition) {
		return new LeftJoin(this, rightOp, condition);
	}
	
	public final RightJoin joinRight(Operator rightOp, Condition condition) {
		return new RightJoin(this, rightOp, condition);
	}
}
