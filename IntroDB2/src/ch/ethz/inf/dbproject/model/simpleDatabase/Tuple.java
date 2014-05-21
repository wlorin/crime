package ch.ethz.inf.dbproject.model.simpleDatabase;

/**
 * A tuple in our database. A tuple consists of a schema (describing the names
 * of the columns) and values. A tuple is created and modified by operators.
 * 
 * You can use String to store the values. In case you need a specific type,
 * you can use the additional getType methods.
 */
public class Tuple {

	private final TupleSchema schema;
	public final String[] values;

	public Tuple(
		final TupleSchema schema, 
		final String[] values
	) {
		this.schema = schema;
		this.values = values;
	}

	public final TupleSchema getSchema() {
		return this.schema;
	}

	public final String get(final int index) {
		return this.values[index];
	}
	
	public final String get(String key) {
		return get(schema.getIndex(key));
	}

	public final short getShort(final int index) {
		return Short.parseShort(this.values[index]);
	}
	
	public final int getInt(final int index) {
		return Integer.parseInt(this.values[index]);
	}
	
	public final float getFloat(final int index) {
		return Float.parseFloat(this.values[index]);
	}
	
	public final double getDouble(final int index) {
		return Double.parseDouble(this.values[index]);
	}
	
	public final boolean isNull(final int index) {
		return this.values[index] == null;
	}

	// TODO 
	// You may add other custom type getters here
	// i.e. Date, Time
	
	public final String toString() {
		final StringBuilder buf = new StringBuilder();
		for (int i = 0; i < values.length; i++) {
			buf.append(schema.names[i]);
			buf.append("=");
			buf.append(values[i]);
			if (i < values.length - 1) {
				buf.append(",");
			}
		}
		return buf.toString();
	}
	
	public final int getTupleSize() {
		int size = schema.types.length / 8 + 1; //flags
		for (int i = 0; i < schema.types.length; i++) {
			if (schema.types[i].variableSize) {
				size += 4;
				size += values[i].length();
			}
			else {
				size += schema.types[i].size;
			}
		}
		return size;
	}

	public Type getType(String name) {
		return schema.getType(schema.getIndex(name));
	}
}
