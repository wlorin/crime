package ch.ethz.inf.dbproject.model.simpleDatabase.operators;

import java.io.FileOutputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ch.ethz.inf.dbproject.model.simpleDatabase.Tuple;
import ch.ethz.inf.dbproject.model.simpleDatabase.TupleSchema;
import ch.ethz.inf.dbproject.model.simpleDatabase.Type;
import ch.ethz.inf.dbproject.model.simpleDatabase.TypeInt;

public class StaticOperators {
	
	public static boolean checkKey(final String fileName, final TupleSchema schema, final String[] values) {
		Type primaryKey = schema.getPrimaryKey();
		if (primaryKey != null) {
			int pos = schema.getIndex(primaryKey.name);
			Operator op = new Select(new Scan(fileName, schema), primaryKey.name, values[pos]);
			if (!op.moveNext()) {
				return true;
			}
		}
		return false;
	}
	public static boolean insert(final String fileName, final TupleSchema schema, final String[] values) {
		assert(schema.types.length == values.length);
		assert(checkKey(fileName, schema, values));
		FileOutputStream writer = null;
		TypeInt myIntConv = new TypeInt("dummy");
		try {
			writer = new FileOutputStream(fileName, true);
			writer.write(0);
			for (int i = 0; i < values.length; i++) {
				int size = schema.types[i].size;
				if (schema.types[i].variableSize) {
					size = Math.min(values[i].length(), size);
					writer.write(myIntConv.toByteArr(size));
				}
				writer.write(schema.types[i].toByteArr(values[i]));
			}
			writer.flush();
			writer.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}
	
	public static int update(final String fileName, final TupleSchema schema, final String[] columns, final String[] values) {
		int updated = 0;
		int last = columns.length-1;
		assert(columns.length == values.length && values.length > 1);
		List<Tuple> deleted = delete(fileName, schema, columns[last], values[last]);
		HashMap<Integer, Integer> updateMap = new HashMap<Integer, Integer>();
		for (int i = 0; i < columns.length; i++) {
			int pos = schema.getIndex(columns[i]);
			if (pos > 0) {
				updateMap.put(pos, i);
			}
		}
		for (Tuple t : deleted) {
			String[] newValues = new String[schema.types.length];
			for (int i = 0; i < schema.types.length; i++) {
				if (updateMap.containsKey(i)) {
					newValues[i] = values[updateMap.get(i)];
				}
				else {
					newValues[i] = t.get(i);
				}
			}
			insert(fileName, schema, newValues);
			updated++;
		}
		return updated;
	}
	
	public static List<Tuple> delete(final String fileName, final TupleSchema schema, final String column, final String compareValue) {
		List<Tuple> res = new ArrayList<Tuple>();
		List<Long> posToDelete = new ArrayList<Long>();
		Scan scan = new Scan(fileName, schema);
		Select select = new Select(scan, column, compareValue);
		while (select.moveNext()) {
			res.add(select.current());
			posToDelete.add(scan.getBufferPosition() - select.current().getTupleSize());
		}
		try {
			RandomAccessFile rand = new RandomAccessFile(fileName, "rw");
			for (Long i : posToDelete) {
				rand.seek(i);
				rand.writeByte(1);
			}
			rand.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}
	
	public static int Count(Operator op) {
		int count = 0;
		while (op.moveNext()) {
			count++;
		}
		return count;
	}
}
