package ch.ethz.inf.dbproject.model.simpleDatabase;

import java.nio.ByteBuffer;

public class TypeInt extends Type<Integer> {
	
	public boolean isAutoIncrement = false;

	public TypeInt() {
		super((char)4, false);
	}

	@Override
	public Class<Integer> getType() {
		return int.class;
	}
	
	@Override
	public byte[] toByteArr(String value) {
		int i = Integer.valueOf(value);
		return toByteArr(i);
	}
	
	public byte[] toByteArr(int i) {
		return ByteBuffer.allocate(4).putInt(i).array();
	}
	
	@Override
	public String fromByteArr(byte[] value) {
		return String.valueOf(getIntFromByteArr(value));
	}
	
	public int getIntFromByteArr(byte[] value) {
		return ByteBuffer.wrap(value).getInt();
	}

	@Override
	public Integer parse(String string) {
		return Integer.valueOf(string);
	}

	@Override
	protected String tToString(Integer t) {
		return "" + t;
	}
}
