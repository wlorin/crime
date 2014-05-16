package ch.ethz.inf.dbproject.model.simpleDatabase.conditional;

import ch.ethz.inf.dbproject.model.simpleDatabase.Tuple;

public class And implements Condition {
	private Condition[] conditionals;
	
	public And(Condition... conditionals) {
		this.conditionals = conditionals;
	}

	@Override
	public boolean matches(Tuple t) {
		for (Condition conditional : conditionals) {
			if(!conditional.matches(t)) {
				return false;
			}
		}
		
		return true;
	}
	
	
}
