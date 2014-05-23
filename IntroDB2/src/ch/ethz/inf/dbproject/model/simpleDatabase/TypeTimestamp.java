package ch.ethz.inf.dbproject.model.simpleDatabase;

public class TypeTimestamp extends Type {

	public TypeTimestamp() {
		super(19, false);
	}

	public TypeTimestamp(boolean isPrimary) {
		super(19, false, isPrimary);
	}

	@Override
	public String getType() {
		return "TimeStamp";
	}

}
