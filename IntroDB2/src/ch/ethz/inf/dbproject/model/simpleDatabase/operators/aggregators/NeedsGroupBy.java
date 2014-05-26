package ch.ethz.inf.dbproject.model.simpleDatabase.operators.aggregators;

import java.util.ArrayList;
import java.util.List;

import ch.ethz.inf.dbproject.model.simpleDatabase.operators.GroupBy;
import ch.ethz.inf.dbproject.model.simpleDatabase.operators.Operator;

public class NeedsGroupBy implements Aggregatorable {
	private List<AggregatorEntry> aggregatorEntries = new ArrayList<>();
	private Operator source;

	public NeedsGroupBy(Operator source, String sourceColumn, Aggregator<?> aggregator) {
		this.source = source;
		add(sourceColumn, aggregator);
	}

	@Override
	public NeedsGroupBy sum(String columnName) {
		add(columnName, new Sum());
		return this;
	}

	private void add(String sourceColumn, Aggregator<?> aggregator) {
		aggregatorEntries.add(new AggregatorEntry(sourceColumn, aggregator));
	}

	@Override
	public NeedsGroupBy max(String columnName) {
		add(columnName, new Max());
		return this;
	}

	@Override
	public NeedsGroupBy min(String columnName) {
		add(columnName, new Min());
		return this;
	}

	@Override
	public NeedsGroupBy count(String columnName) {
		add(columnName, new Count());
		return this;
	}
	
	public Aggregatorable as(String alias) {
		aggregatorEntries.get(aggregatorEntries.size() - 1).targetColumn = alias;
		return this;
	}
	
	public static class AggregatorEntry {
		public String sourceColumn;
		public String targetColumn;
		public Aggregator<?> aggregator;
		
		private AggregatorEntry(String sourceColumn, Aggregator<?> aggregator) {
			this.sourceColumn = sourceColumn;
			this.targetColumn = sourceColumn;
			this.aggregator = aggregator;
		}
	}

	@Override
	public Operator groupBy(String... columnNames) {
		return new GroupBy(source, aggregatorEntries, columnNames);
	}
}
