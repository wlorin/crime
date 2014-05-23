package ch.ethz.inf.dbproject.model.simpleDatabase.operators;

import static ch.ethz.inf.dbproject.model.simpleDatabase.conditional.Static.*;

import java.io.FileOutputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ch.ethz.inf.dbproject.model.simpleDatabase.Tuple;
import ch.ethz.inf.dbproject.model.simpleDatabase.TupleSchema;
import ch.ethz.inf.dbproject.model.simpleDatabase.TupleSchema.TupleSchemaBuilder.SchemaColumn;
import ch.ethz.inf.dbproject.model.simpleDatabase.TypeInt;
import ch.ethz.inf.dbproject.model.simpleDatabase.conditional.Condition;

import com.google.common.collect.ImmutableList;

public class StaticOperators {
	
	public static boolean checkKey(final String fileName, final TupleSchema schema, final String[] values) {
		ImmutableList<SchemaColumn> primaryKeys = schema.primaryKeys;
		if (!primaryKeys.isEmpty()) {
			Condition condition = null;
			for (SchemaColumn primaryKey : primaryKeys) {
				Condition newCondition = eq(col(primaryKey.name), val(values[schema.getIndex(primaryKey.name)]));
				if (condition != null) {
					condition = and(condition, newCondition);
				}
				else {
					condition = newCondition;
				}
			}
			Operator op = select(new Scan(fileName, schema), condition);
			return !op.moveNext();
		}
		return true;
	}
	private static int getNextAutoIncrement(final String fileName, final TupleSchema schema) {
		int res = 0;
		int autoIncIndex = schema.getAutoIncrementColumn();
		assert(autoIncIndex >= 0);
		Scan scan = new Scan(fileName, schema);
		while (scan.moveNext()) {
			res = Math.max(res, scan.current().getInt(autoIncIndex));
		}
		return res + 1;
	}
	public static boolean insert(final String fileName, final TupleSchema schema, final String[] values) {
		assert(schema.types.length == values.length);
		assert(checkKey(fileName, schema, values));
		FileOutputStream writer = null;
		TypeInt myIntConv = new TypeInt();
		try {
			byte flags[] = new byte[values.length / 8 + 1];
			int autoIncIndex = schema.getAutoIncrementColumn();
			for (int i = 0; i < values.length; i++) {
				int idx = (i + 1) / 8;
				if (values[i] == null) {
					if (autoIncIndex >= 0 && i == autoIncIndex) {
						values[i] = String.valueOf(getNextAutoIncrement(fileName, schema));
					}
					else {
						flags[idx] = (byte) (flags[idx] | (0b10000000 >> ((i+1) % 8)));
					}
				}
			}
			writer = new FileOutputStream(fileName, true);
			writer.write(flags);
			for (int i = 0; i < values.length; i++) {
				int size = schema.types[i].size;
				if (schema.types[i].variableSize) {
					if (values[i] == null) {
						size = 0;
					}
					else {
						size = Math.min(values[i].length(), size);
					}
					writer.write(myIntConv.toByteArr(size));
				}
				if (values[i] != null) {
					writer.write(schema.types[i].toByteArr(values[i]));
				}
			}
			writer.flush();
			writer.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}
	
	public static int update(final String fileName, final TupleSchema schema, final String[] columns, final String[] values, Condition condition) {
		int updated = 0;
		if(columns.length != values.length) {
			throw new IllegalArgumentException("Columns and values don't pair up.");
		}
		
		if (columns.length == 0 || values.length == 0) {
			throw new IllegalArgumentException("Must provide at least one column/value-pair to update");
		}
		
		List<Tuple> deleted = delete(fileName, schema, condition);
		HashMap<Integer, Integer> updateMap = new HashMap<Integer, Integer>();
		for (int i = 0; i < columns.length; i++) {
			int pos = schema.getIndex(columns[i]);
			updateMap.put(pos, i);
		}
		for (Tuple t : deleted) {
			String[] newValues = new String[schema.types.length];
			for (int i = 0; i < schema.types.length; i++) {
				if (updateMap.containsKey(i)) {
					newValues[i] = values[updateMap.get(i)];
				}
				else {
					newValues[i] = t.getString(i);
				}
			}
			insert(fileName, schema, newValues);
			updated++;
		}
		return updated;
	}
	
	public static List<Tuple> delete(final String fileName, final TupleSchema schema, Condition condition) {
		List<Tuple> res = new ArrayList<Tuple>();
		List<Long> posToDelete = new ArrayList<Long>();
		Scan scan = new Scan(fileName, schema);
		Select select = new Select(scan, condition);
		while (select.moveNext()) {
			res.add(select.current());
			posToDelete.add(scan.getBufferPosition() - select.current().getTupleSize());
		}
		try {
			RandomAccessFile rand = new RandomAccessFile(fileName, "rw");
			for (Long i : posToDelete) {
				rand.seek(i);
				int flag = rand.read() | 0b10000000;
				rand.seek(i);
				rand.writeByte(flag);
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
	
	public static Select select(Operator operator, Condition condition) {
		return new Select(operator, condition);
	}
	
	public static Case project(Operator operator, String... column) {
		return new Case(operator, column);
	}
	
	public static InnerJoin innerJoin(Operator leftOp, Operator rightOp, Condition condition) {
		return new InnerJoin(leftOp, rightOp, condition);
	}
	
	public static Sort sortAsc(Operator operator, String column) {
		return new Sort(operator, column, true);
	}
	
	public static Sort sortDesc(Operator operator, String column) {
		return new Sort(operator, column, false);
	}
}
