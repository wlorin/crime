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
	
	public Case(final Operator op, final String... columns) {
		super(makeNewSchema(op.schema, columns));
		this.op = op;
	}
	public Case (TupleSchema schema, final Operator op, final TupleSchema newSchema) {
		super(newSchema);
		this.op = op;
	}
	
	@Override
	public boolean moveNext() {
		if (op.moveNext()) {
			Tuple current = op.current();
			String[] values = new String[schema.types.length];
			int i = 0;
			for (SchemaColumn column : schema.columns) {
				values[i] = current.get(column.name);
				i++;
			}
			this.current = new Tuple(schema, values);
			return true;
		}
		return false;
	}

	private static TupleSchema makeNewSchema(TupleSchema schema, String[] columns) {
		TupleSchemaBuilder builder = TupleSchema.build();
		for (String columnName : columns) {
			Type type = schema.getType(columnName);
			builder.with(columnName, type);
		}
		return builder.build();
	}
}
