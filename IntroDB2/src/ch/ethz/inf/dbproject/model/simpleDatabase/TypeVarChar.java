package ch.ethz.inf.dbproject.model.simpleDatabase;

public class TypeVarChar extends Type<String> {

	public TypeVarChar(int size) {
		super(size, true);
	}

	@Override
	public Class<String> getType() {
		return String.class;
	}

	@Override
	public String parse(String string) {
		return string;
	}

	@Override
	protected String tToString(String string) {
		return string;
	}

}
