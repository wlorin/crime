package ch.ethz.inf.dbproject.model.simpleDatabase.operators;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import ch.ethz.inf.dbproject.model.simpleDatabase.Tuple;
import ch.ethz.inf.dbproject.model.simpleDatabase.TupleSchema;
import ch.ethz.inf.dbproject.model.simpleDatabase.TupleSchema.TupleSchemaBuilder;
import ch.ethz.inf.dbproject.model.simpleDatabase.TupleSchema.TupleSchemaBuilder.SchemaColumn;
import ch.ethz.inf.dbproject.model.simpleDatabase.conditional.Condition;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.ObjectArrays;

public abstract class Join extends Operator {

	final List<Tuple> lefts;
	final List<Tuple> rights;
	private int nextIndex = 0;
	private List<Tuple> joined;
	
	public Join(Operator leftOp, Operator rightOp, Condition condition) {
		super(mergeSchema(leftOp.schema, rightOp.schema));
		lefts = new ArrayList<Tuple>();
		while (leftOp.moveNext()) {
			lefts.add(leftOp.current());
		}
		rights = new ArrayList<Tuple>();

		while (rightOp.moveNext()) {
			rights.add(rightOp.current());
		}
		
		if (lefts.size() == 0 || rights.size() == 0) {
			return;
		}
		
		TreeSet<Integer> leftIndices = new TreeSet<>();
		for (int i = 0; i < lefts.size(); i++) {
			leftIndices.add(i);
		}
		TreeSet<Integer> rightIndices = new TreeSet<>();
		for (int i = 0; i < rights.size(); i++) {
			rightIndices.add(i);
		}
		
		int leftIndex = 0;
		
		joined = Lists.newArrayList();
		for (Tuple left : lefts) {
			
			int rightIndex = 0;
			for (Tuple right : rights) {
				Tuple tuple = new Tuple(schema, ObjectArrays.concat(left.values, right.values, String.class));
				if (condition.matches(tuple)) {
					joined.add(tuple);
					leftIndices.remove(leftIndex);
					rightIndices.remove(rightIndex);
				}
				rightIndex++;
			}
			leftIndex++;
		}
		
		TreeSet<Integer> unusedLeftIndices = leftIndices;
		TreeSet<Integer> unusedRightIndices = rightIndices;
		
		List<Tuple> unprocessedLeft = unprocessed(lefts, unusedLeftIndices);
		handleUnprocessedLeft(joined, unprocessedLeft);
		List<Tuple> unprocessedRight = unprocessed(rights, unusedRightIndices);
		handleUnprocessedRight(joined, unprocessedRight);
	}


	protected abstract void handleUnprocessedLeft(List<Tuple> joined, List<Tuple> unprocessedLeft);
	
	protected abstract void handleUnprocessedRight(List<Tuple> joined, List<Tuple> unprocessedRight);


	private List<Tuple> unprocessed(List<Tuple> ts, TreeSet<Integer> unusedIndices) {
		ArrayList<Tuple> unprocessed = new ArrayList<>();
		for (Integer index : unusedIndices) {
			unprocessed.add(ts.get(index));
		}
		return unprocessed;
	}

	private static TupleSchema mergeSchema(TupleSchema leftSchema, TupleSchema rightSchema) {
		TupleSchemaBuilder builder = TupleSchema.build();
		for (SchemaColumn schemaColumn : allCollumns(leftSchema, rightSchema)) {
			builder.with(schemaColumn.name, schemaColumn.type);
		}
		return builder.build();
	}

	private static ArrayList<SchemaColumn> allCollumns(TupleSchema leftSchema, TupleSchema rightSchema) {
		return Lists.newArrayList(Iterables.concat(leftSchema.columns, rightSchema.columns));
	}
	
	@Override
	public boolean moveNext() {
		if (nextIndex < joined.size()) {
			current = joined.get(nextIndex);
			nextIndex++;
			return true;
		}
		return false;
	}
}
