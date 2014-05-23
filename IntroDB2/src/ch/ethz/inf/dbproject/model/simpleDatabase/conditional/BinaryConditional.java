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
		//TODO: This check is nice in theroy, but it makes dynamic conditions impossible (i.e. keyCheck in StaticOperators - keyCheck does not know the type of it's key columns)
		//if (a.valueType(t).getClass() != b.valueType(t).getClass()) throw new IllegalArgumentException("Incompatible types: " + a.valueType(t).getClass().getSimpleName() + " " + b.valueType(t).getClass().getSimpleName());
		
		String aValue = a.value(t);
		String bValue = b.value(t);
		
		if (aValue == null && bValue == null) return true;
		
		return bothNotNull(aValue, bValue);
	}

	protected abstract boolean bothNotNull(String aValue, String bValue);

}