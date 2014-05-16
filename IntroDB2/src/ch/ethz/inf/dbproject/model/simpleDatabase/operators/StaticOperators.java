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
		TypeInt myIntConv = new TypeInt();
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
					newValues[i] = t.get(i);
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
