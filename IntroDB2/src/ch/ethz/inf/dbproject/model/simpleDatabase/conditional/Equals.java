package ch.ethz.inf.dbproject.model.simpleDatabase.conditional;


public class Equals extends BinaryConditional implements Condition {
	public Equals(ConditionalSource a, ConditionalSource b) {
		super(a, b);
	}
	
	@Override
	protected boolean bothNotNull(String aValue, String bValue) {
		return aValue.equals(bValue);
	}
}
