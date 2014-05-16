package ch.ethz.inf.dbproject.model.simpleDatabase.conditional;

import ch.ethz.inf.dbproject.model.simpleDatabase.Tuple;

public class Or implements Condition {
	private Condition[] conditionals;

	public Or(Condition... conditionals) {
		this.conditionals = conditionals;
	}

	@Override
	public boolean matches(Tuple t) {
		for (Condition conditional : conditionals) {
			if(conditional.matches(t)) {
				return true;
			}
		}
		return false;
	}
	
	
}
