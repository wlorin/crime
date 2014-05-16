package ch.ethz.inf.dbproject.model.simpleDatabase;

import java.io.Serializable;
import java.util.HashMap;

/**
 * The schema contains meta data about a tuple. So far we only store the name of
 * each column. Other meta data, such cardinalities, indexes, etc. could be
 * specified here.
 */
public class TupleSchema implements Serializable {

	public final Type[] types;
	private final HashMap<String, Integer> columnNamesMap;
	private Type primaryKey = null;
	
	public Type getPrimaryKey() {
		return primaryKey;
	}
	
	public TupleSchema(final Type[] types) {
		this.types = types;
		this.columnNamesMap = new HashMap<String, Integer>();
		for (int i = 0; i < types.length; ++i) {
			this.columnNamesMap.put(this.types[i].name.toUpperCase(), i);
			if (types[i].isPrimaryKey) {
				primaryKey = types[i];
			}
		}
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
			return -1; // error
		} else {
			return index;
		}
		
	}
	
	public Type getType(int index) {
		return types[index];
	}
}
