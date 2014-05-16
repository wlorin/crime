package ch.ethz.inf.dbproject.model.simpleDatabase;

public class TypeVarChar extends Type {

	public TypeVarChar(String name, int size) {
		super(name, size, true);
	}

	@Override
	public String getType() {
		return "Varchar(" + size + ")";
	}

}
