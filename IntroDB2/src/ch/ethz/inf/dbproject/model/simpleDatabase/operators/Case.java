package ch.ethz.inf.dbproject.model.simpleDatabase.operators;

import ch.ethz.inf.dbproject.model.simpleDatabase.Tuple;
import ch.ethz.inf.dbproject.model.simpleDatabase.TupleSchema;
import ch.ethz.inf.dbproject.model.simpleDatabase.TupleSchema.TupleSchemaBuilder;
import ch.ethz.inf.dbproject.model.simpleDatabase.TupleSchema.TupleSchemaBuilder.SchemaColumn;
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
	
	@Override
	public boolean moveNext() {
		if (op.moveNext()) {
			if (newSchema == null) {
				makeNewSchema();
			}
			Tuple current = op.current();
			String[] values = new String[newSchema.types.length];
			int i = 0;
			for (SchemaColumn column : newSchema.columns) {
				values[i] = current.get(column.name);
				i++;
			}
			this.current = new Tuple(newSchema, values);
			return true;
		}
		return false;
	}

	private void makeNewSchema() {
		TupleSchemaBuilder builder = TupleSchema.build();
		for (String columnName : columns) {
			Type type = op.current.getSchema().getType(columnName);
			builder.with(columnName, type);
		}
		newSchema = builder.build();
	}
}
