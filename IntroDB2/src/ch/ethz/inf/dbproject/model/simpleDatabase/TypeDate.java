package ch.ethz.inf.dbproject.model.simpleDatabase;

public class TypeDate extends Type {

	public TypeDate() {
		super(10, false);
	}

	@Override
	public String getType() {
		return "Date";
	}

}
