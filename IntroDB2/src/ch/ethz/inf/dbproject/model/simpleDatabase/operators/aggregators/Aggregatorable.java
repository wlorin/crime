package ch.ethz.inf.dbproject.model.simpleDatabase.operators.aggregators;

import ch.ethz.inf.dbproject.model.simpleDatabase.operators.Operator;

public interface Aggregatorable {
	public NeedsGroupBy sum(String columnName);
	public NeedsGroupBy max(String columnName);
	public NeedsGroupBy min(String columnName);
	public NeedsGroupBy avg(String columnName);
	public NeedsGroupBy count(String columnName);
	
	public Operator groupBy(String... columnNames);
}
