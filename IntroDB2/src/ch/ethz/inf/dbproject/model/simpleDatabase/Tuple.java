package ch.ethz.inf.dbproject.model.simpleDatabase;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

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

	public final String getString(final int index) {
		return this.values[index];
	}
	
	public final String getString(String key) {
		return getString(schema.getIndex(key));
	}
	
	public final Integer getInt(final String index) {
		return getInt(schema.getIndex(index));
	}

	public final Integer getInt(final int index) {
		if (isNull(index)) {
			return null;
		}
		return Integer.parseInt(this.values[index]);
	}
	
	public final boolean isNull(final int index) {
		return this.values[index] == null;
	}

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
	
	public final Date getDate(final String index) {
		return getDate(schema.getIndex(index));
	}
	public final Date getDate(final int index) {
		if (isNull(index)) {
			return null;
		}
		try {
			return new SimpleDateFormat("yyyy-MM-dd").parse(values[index]);
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public final Time getTime(final String index) {
		return getTime(schema.getIndex(index));
	}
	public final Time getTime(final int index) {
		if (isNull(index)) {
			return null;
		}
		try {
			return (Time)new SimpleDateFormat("hh:mm:ss").parse(values[index]);
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public final Timestamp getTimestamp(final String index) {
		return getTimestamp(schema.getIndex(index));
	}
	public final Timestamp getTimestamp(final int index) {
		if (isNull(index)) {
			return null;
		}
		return Timestamp.valueOf(values[index]);
	}
}
