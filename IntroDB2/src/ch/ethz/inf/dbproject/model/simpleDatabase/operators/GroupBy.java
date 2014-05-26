package ch.ethz.inf.dbproject.model.simpleDatabase.operators;

import ch.ethz.inf.dbproject.model.simpleDatabase.Tuple;
import ch.ethz.inf.dbproject.model.simpleDatabase.TupleSchema;
import ch.ethz.inf.dbproject.model.simpleDatabase.TupleSchema.TupleSchemaBuilder;
import ch.ethz.inf.dbproject.model.simpleDatabase.TupleSchema.TupleSchemaBuilder.SchemaColumn;
import ch.ethz.inf.dbproject.model.simpleDatabase.operators.aggregators.Aggregator;
import ch.ethz.inf.dbproject.model.simpleDatabase.operators.aggregators.NeedsGroupBy.AggregatorEntry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import com.google.common.collect.Lists;

public class GroupBy extends Operator {
	private List<Tuple> tuples = new ArrayList<>();
	private int nextIndex;
	public GroupBy(Operator source, List<AggregatorEntry> aggregators, String[] groupByColumnsArr) {
		super(generateSchema(source, aggregators, groupByColumnsArr));
		
		List<String> groupByColumns = Lists.newArrayList(groupByColumnsArr);
		groupAll(source, aggregators, groupByColumns);
    }
	
	private class AggregatorResultPair<T> {
		final Aggregator<T> aggregator;
		T result;
		final String source;
		
		@SuppressWarnings("unchecked")
		public AggregatorResultPair(AggregatorEntry aggregatorEntry, Tuple tuple) {
			this.aggregator = (Aggregator<T>) aggregatorEntry.aggregator;
			this.source = aggregatorEntry.sourceColumn;
			result = aggregator.init(aggregator.getSchemaType().parse(tuple.getString(source)));
		}

		public void process(Tuple current) {
			result = aggregator.aggregate(result, aggregator.getSchemaType().parse(current.getString(source)));
		}
		
		public String resultToString() {
			return aggregator.getSchemaType().store(result);
		}
	}
	
	private void groupAll(Operator source2, List<AggregatorEntry> aggregators,
			List<String> groupByColumns) {
		
		HashMap<List<String>, HashMap<String, AggregatorResultPair<?>>> aggregatesByGroup = new HashMap<>();
		while(source2.moveNext()) {
			List<String> groupKey = buildKey(source2.current, groupByColumns);
			if (!aggregatesByGroup.containsKey(groupKey)) {
				addAggregatorsForKey(groupKey, aggregatesByGroup, aggregators, source2.current);
			}
			else {
				Set<Entry<String, AggregatorResultPair<?>>> aggregatorSet = aggregatesByGroup.get(groupKey).entrySet();
				for (Entry<String, AggregatorResultPair<?>> entry : aggregatorSet) {
					AggregatorResultPair<?> aggregatorResultPair = entry.getValue();
					aggregatorResultPair.process(source2.current);
				}
			}
		}
		
		Set<Entry<List<String>, HashMap<String, AggregatorResultPair<?>>>> results = aggregatesByGroup.entrySet();
		
		for (Entry<List<String>, HashMap<String, AggregatorResultPair<?>>> entry : results) {
			List<String> values = new ArrayList<>();
			
			List<String> groupValues = entry.getKey();
			values.addAll(groupValues);
			
			for (AggregatorEntry aggregatorEntry : aggregators) {
				values.add(entry.getValue().get(aggregatorEntry.targetColumn).resultToString());
			}
			String[] valueArray = values.toArray(new String[0]);
			tuples.add(new Tuple(schema, valueArray));
		}
	}

	private void addAggregatorsForKey(List<String> groupKey,
			HashMap<List<String>, HashMap<String, AggregatorResultPair<?>>> x,
			List<AggregatorEntry> aggregators, Tuple tuple) {
		HashMap<String, AggregatorResultPair<?>> targetColum2AggregatorResultPair = new HashMap<>();
		for (AggregatorEntry aggregatorEntry : aggregators) {
			targetColum2AggregatorResultPair.put(
					aggregatorEntry.targetColumn, 
					new AggregatorResultPair<Object>(aggregatorEntry, tuple)
			);
		}
		x.put(groupKey, targetColum2AggregatorResultPair);
	}

	private List<String> buildKey(Tuple current, List<String> groupByColumns) {
		List<String> result = new ArrayList<>();
		for (String column : groupByColumns) {
			result.add(current.getString(column));
		}
		
		return result;
	}

	static private TupleSchema generateSchema(Operator source,
			List<AggregatorEntry> aggregators, String[] groupByColumnsArr) {
		List<String> groupByColumns = new ArrayList<>();
		for (int i = 0; i < groupByColumnsArr.length; i++) {
			groupByColumns.add(groupByColumnsArr[i]);
		}
		
		HashSet<String> setOfGroupByCols = new HashSet<String>(groupByColumns);
		if (setOfGroupByCols.size() != groupByColumns.size()) {
			throw new IllegalArgumentException("Duplicate group By Columns");
		}
		
		HashSet<String> originalColumns = new HashSet<>();
		for (SchemaColumn schemaColumn : source.schema.columns) {
			originalColumns.add(schemaColumn.name);
		}
		
		if (!originalColumns.containsAll(setOfGroupByCols)) {
			throw new IllegalArgumentException("Invalid group by columns");
		}
		
		HashSet<String> remainingOldColumns = new HashSet<>(originalColumns);
		remainingOldColumns.retainAll(setOfGroupByCols);
		
		HashSet<String> newColumns = new HashSet<>(remainingOldColumns);
		
		for (AggregatorEntry aggregatorEntry : aggregators) {
			if (!originalColumns.contains(aggregatorEntry.sourceColumn)) {
				throw new IllegalArgumentException("Source column does not exist: " + aggregatorEntry.sourceColumn);
			}
			if (newColumns.contains(aggregatorEntry.targetColumn)) {
				throw new IllegalArgumentException("Name with column " + aggregatorEntry.targetColumn + " already exists");
			}
			newColumns.add(aggregatorEntry.targetColumn);
		}
		
		TupleSchemaBuilder builder = TupleSchema.build();
		
		for (String remainingColumn : remainingOldColumns) {
			builder.with(remainingColumn, source.schema.getType(remainingColumn));
		}
		
		for (AggregatorEntry aggregatorEntry : aggregators) {
			builder.with(aggregatorEntry.targetColumn, aggregatorEntry.aggregator.getSchemaType());
		}
		
		
		return builder.build();
	}

	@Override
	public boolean moveNext() {
		if (nextIndex < tuples.size()) {
			current = tuples.get(nextIndex);
			nextIndex++;
			return true;
		}
		return false;
	}
}
