package ch.ethz.inf.dbproject.model.simpleDatabase.conditional;

import ch.ethz.inf.dbproject.model.simpleDatabase.Tuple;
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
		return new Value(s, new TypeVarChar(s != null ? s.length() : 1));
	}
	
	public static Equals eq(ConditionalSource a, ConditionalSource b) {
		return new Equals(a, b);
	}
	public static Like like(ConditionalSource a, ConditionalSource b) {
		return new Like(a, b);
	}
	
	public static Condition all() {
		return new Condition() {
			
			@Override
			public boolean matches(Tuple t) {
				return true;
			}
		};
	}
	
	public static Condition none() {
		return new Condition() {
			
			@Override
			public boolean matches(Tuple t) {
				return false;
			}
		};
	}
}
