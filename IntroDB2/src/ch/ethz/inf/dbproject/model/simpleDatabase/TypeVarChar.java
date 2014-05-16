package ch.ethz.inf.dbproject.model.simpleDatabase;

public class TypeVarChar extends Type {

	public TypeVarChar(int size) {
		super(size, true);
	}

	@Override
	public String getType() {
		return "Varchar(" + size + ")";
	}

}
