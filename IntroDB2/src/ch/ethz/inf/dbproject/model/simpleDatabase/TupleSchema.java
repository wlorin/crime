package ch.ethz.inf.dbproject.model.simpleDatabase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import ch.ethz.inf.dbproject.model.simpleDatabase.TupleSchema.TupleSchemaBuilder.SchemaColumn;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

/**
 * The schema contains meta data about a tuple. So far we only store the name of
 * each column. Other meta data, such cardinalities, indexes, etc. could be
 * specified here.
 */
public class TupleSchema implements Serializable {

	public final Type[] types;
	private final HashMap<String, Integer> columnNamesMap;
	private final HashMap<String, Type> nameToType;
	public ImmutableList<SchemaColumn> primaryKeys = null;
	String[] names;
	public final List<SchemaColumn> columns;
	
	private TupleSchema(List<SchemaColumn> columns) {		
		this.columns = ImmutableList.copyOf(columns);
		this.columnNamesMap = new HashMap<String, Integer>();
		this.nameToType = new HashMap<>();
		names= new String[columns.size()];
		types = new Type[columns.size()];
		
		List<SchemaColumn> keys = Lists.newArrayList();
		for (int i = 0; i < types.length; ++i) {
			final SchemaColumn column = columns.get(i);
			names[i] = column.name;
			types[i] = column.type;
			nameToType.put(names[i], types[i]);
			this.columnNamesMap.put(names[i].toUpperCase(), i);
			if (types[i].isPrimaryKey) {
				keys.add(column);
			}
		}
		
		primaryKeys = ImmutableList.copyOf(keys);
		
		
	}
	
	/**
	 * Given the name of a column, returns the index in the respective tuple.
	 * 
	 * @param column column name
	 * @return index of column in tuple
	 */
	public int getIndex(final String column) {

		final Integer index = this.columnNamesMap.get(column.toUpperCase());
		if (index == null) {
			throw new IllegalArgumentException("Column " + column + " does not exist");
		} else {
			return index;
		}
	}
	
	public int getAutoIncrementColumn() {
		for (int i = 0; i < types.length; i++) {
			if (types[i].isPrimaryKey && types[i] instanceof TypeInt) {
				TypeInt typeint = (TypeInt)types[i];
				if (typeint.isAutoIncrement) {
					return i;
				}
			}
		}
		return -1;
	}
	
	public Type getType(int index) {
		return types[index];
	}
	
	public TupleSchema prefixed(String prefix) {
		if (prefix.contains("\\.")) {
			throw new IllegalArgumentException("Prefix must not contain '.'");
		}
		
		TupleSchemaBuilder tupleSchemaBuilder = new TupleSchemaBuilder();
		for (int i = 0; i < types.length; i++) {
			String name = names[i];
			Type type = types[i];
			String newName = prefix + "." + name;
			if (name.contains("\\.")) {
				newName = prefix + "." + name.split("\\.")[1];
			}
			tupleSchemaBuilder.with(newName, type);
		}
		
		return tupleSchemaBuilder.build();
	}
	
	public static TupleSchemaBuilder build() {
		return new TupleSchemaBuilder();
	}
	
	public static class TupleSchemaBuilder {
		List<SchemaColumn> columns = new ArrayList<>();
		TupleSchema tupleSchema;
		
		boolean built = false;
		
		public TupleSchemaBuilder with(String name, Type type) {
			if (built) {
				throw new IllegalStateException("Schema already built");
			}
			columns.add(new SchemaColumn(name, type));
			return this;
		}
		
		public TupleSchemaBuilder intCol(String name) {
			return with(name, new TypeInt());
		}
		
		public TupleSchemaBuilder varcharCol(String name, int size) {
			return with(name, new TypeVarChar(size));
		}
		
		public TupleSchemaBuilder dateCol(String name) {
			return with(name, new TypeDate());
		}
		
		public TupleSchemaBuilder timeCol(String name) {
			return with(name, new TypeTime());
		}
		public TupleSchemaBuilder timestampCol(String name) {
			return with(name, new TypeTimestamp());
		}
		
		public TupleSchemaBuilder asPrimary() {
			if (built) {
				throw new IllegalStateException("Schema already built");
			}
			columns.get(columns.size() - 1).type.isPrimaryKey = true;
			return this;
		}
		
		public TupleSchemaBuilder asAutoIncrement() {
			if (built) {
				throw new IllegalStateException("Schema already built");
			}
			Type type = columns.get(columns.size() - 1).type;
			if (!(type instanceof TypeInt)) {
				throw new IllegalStateException("Can only set TypeInt type to autoincrement");
			}
			TypeInt typeInt = (TypeInt) type;
			typeInt.isAutoIncrement = true;
			return this;
		}
		
		public TupleSchemaBuilder markPrimary(String... columnsToMark) {
			if (built) {
				throw new IllegalStateException("Schema already built");
			}
			nextColumn:
			for (String name : columnsToMark) {
				for (SchemaColumn schemaColumn : columns) {
					if (schemaColumn.name.equals(name)) {
						schemaColumn.type.isPrimaryKey = true;
						continue nextColumn;
					}
				}
				throw new IllegalArgumentException("Column " + name + " does not exist");
			}
			return this;
		}
		
		public TupleSchema build() {
			if (built) {
				throw new IllegalStateException("Schema already built");
			}
			HashSet<String> names = new HashSet<>();
			for (SchemaColumn column : columns) {
				if (names.contains(column.name)) {
					throw new IllegalStateException("Duplicate names detected: " + column.name);
				}
				names.add(column.name);
			}
			
			built = true;
			tupleSchema = new TupleSchema(columns);
			return tupleSchema;
		}
		
		public class SchemaColumn {
			public final String name;
			public final Type type;

			public SchemaColumn(String name, Type type) {
				this.name = name;
				this.type = type;
			}
			
			public TupleSchema getSchema() {
				if (!TupleSchemaBuilder.this.built) {
					throw new IllegalStateException("Schema has not yet been built");
				}
				
				return TupleSchemaBuilder.this.tupleSchema;
			}
		}
	}

	public Type getType(String columnName) {
		return getType(getIndex(columnName));
	}
}
