package ch.ethz.inf.dbproject.model.simpleDatabase.conditional;

import ch.ethz.inf.dbproject.model.simpleDatabase.TypeInt;
import ch.ethz.inf.dbproject.model.simpleDatabase.TypeVarChar;

public class Static {
	public static And and(Condition... conditionals) {
		return new And(conditionals);
	}
	
	public static Or or(Condition... conditionals) {
		return new Or(conditionals);
	}
	
	public static ColumnReference col(String name) {
		return new ColumnReference(name);
	}
	
	public static Value val(int i) {
		return new Value(Integer.toString(i), new TypeInt());
	}
	
	public static Value val(String s) {
		// TODO Fix length
		return new Value(s, new TypeVarChar(255));
	}
	
	public static Equals eq(ConditionalSource a, ConditionalSource b) {
		return new Equals(a, b);
	}
	
}
