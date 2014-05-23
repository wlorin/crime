package ch.ethz.inf.dbproject.model.simpleDatabase.operators;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import ch.ethz.inf.dbproject.model.simpleDatabase.Tuple;

/**
 * An empty sort operator
 * For the purposes of the project, you can consider only string comparisons of
 * the values.
 */
public class Sort extends Operator implements Comparator<Tuple> {

	private final Operator op;
	private final String column;
	private final boolean ascending;
	private final ArrayList<Tuple> sortBuffer;
	private int pos = -1;
	
	public Sort(
		final Operator op,
		final String column,
		final boolean ascending
	) {
		super(op.schema);
		this.op = op;
		this.column = column;
		this.ascending = ascending;
		this.sortBuffer = new ArrayList<Tuple>();
	}
	
	@Override
	public final int compare(
		final Tuple l, 
		final Tuple r
	) {
		
		final int columnIndex = l.getSchema().getIndex(this.column);
		
		final int result = 
			l.getString(columnIndex).compareToIgnoreCase(r.getString(columnIndex));
		
		if (this.ascending) {
			return result;
		} else {
			return -result;
		}
	}

	@Override
	public boolean moveNext() {
		if (pos == -1) {
			while (op.moveNext()) {
				sortBuffer.add(op.current());
			}
			Collections.sort(this.sortBuffer, this);
			pos = 0;
		}
		if (sortBuffer.size() > pos) {
			this.current = sortBuffer.get(pos);
			pos++;
			return true;
		}
		return false;
	}

	
}
