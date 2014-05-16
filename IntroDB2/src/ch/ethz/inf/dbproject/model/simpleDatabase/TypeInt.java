package ch.ethz.inf.dbproject.model.simpleDatabase;

import java.nio.ByteBuffer;

public class TypeInt extends Type {

	public TypeInt() {
		super((char)4, false);
	}

	@Override
	public String getType() {
		return "int";
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
}
