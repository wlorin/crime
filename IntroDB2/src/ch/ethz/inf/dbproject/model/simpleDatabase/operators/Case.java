package ch.ethz.inf.dbproject.model.simpleDatabase.operators;

import ch.ethz.inf.dbproject.model.simpleDatabase.Tuple;
import ch.ethz.inf.dbproject.model.simpleDatabase.TupleSchema;
import ch.ethz.inf.dbproject.model.simpleDatabase.Type;

/**
 * Projection in relational algebra. Returns tuples that contain on projected
 * columns. Therefore the new tuples conform to a new schema.
 */
public final class Case extends Operator {

	private final Operator op;
	private TupleSchema newSchema = null;
	private final String[] columns;
	
	public Case(final Operator op, final String column) {
		this.op = op;
		this.columns = new String[] {column};
	}
	
	public Case(final Operator op, final String[] columns) {
		this.op = op;
		this.columns = columns;
	}
	public Case (final Operator op, final TupleSchema newSchema) {
		this.op = op;
		this.newSchema = newSchema;
		this.columns = null;
	}
	
	private void makeNewSchema() {
		Tuple t = op.current();
		Type[] types = new Type[columns.length];
		for (int i = 0; i < types.length; i++) {
			types[i] = t.getSchema().types[t.getSchema().getIndex(columns[i])];
		}
		newSchema = new TupleSchema(types);
	}


	@Override
	public boolean moveNext() {
		if (op.moveNext()) {
			if (newSchema == null) {
				makeNewSchema();
			}
			Tuple current = op.current();
			String[] values = new String[newSchema.types.length];
			for (int i = 0; i < newSchema.types.length; i++) {
				int idx = current.getSchema().getIndex(newSchema.types[i].name);
				values[i] = current.get(idx);
			}
			this.current = new Tuple(newSchema, values);
			return true;
		}
		return false;
	}
}
