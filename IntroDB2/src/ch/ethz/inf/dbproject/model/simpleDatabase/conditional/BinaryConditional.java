package ch.ethz.inf.dbproject.model.simpleDatabase.conditional;

import ch.ethz.inf.dbproject.model.simpleDatabase.Tuple;

public abstract class BinaryConditional implements Condition {

	protected final ConditionalSource a;
	protected final ConditionalSource b;

	public BinaryConditional(ConditionalSource a, ConditionalSource b) {
		this.a = a;
		this.b = b;
	}
	
	@Override
	public boolean matches(Tuple t) {
		if (a.valueType(t).getClass() != b.valueType(t).getClass()) return false;
		
		String aValue = a.value(t);
		String bValue = b.value(t);
		
		if (aValue == null && bValue == null) return true;
		
		return bothNotNull(aValue, bValue);
	}

	protected abstract boolean bothNotNull(String aValue, String bValue);

}