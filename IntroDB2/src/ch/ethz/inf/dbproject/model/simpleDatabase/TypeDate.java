package ch.ethz.inf.dbproject.model.simpleDatabase;

public class TypeDate extends Type {

	public TypeDate(String name) {
		super(name, 10, false);
	}

	@Override
	public String getType() {
		return "Date";
	}

}
