package ch.ethz.inf.dbproject.model.simpleDatabase;

public class TypeTime extends Type {

	public TypeTime(String name) {
		super(name, 8, false);
	}

	@Override
	public String getType() {
		return "Time";
	}

}
