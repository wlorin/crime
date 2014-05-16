package ch.ethz.inf.dbproject.model.simpleDatabase;

import java.nio.charset.Charset;

public abstract class Type {
	public final int size;
	public final boolean variableSize;
	public final String name;
	public boolean isPrimaryKey = false;
	
	public Type (String name, int size, boolean variableSize) {
		this.size = size;
		this.variableSize = variableSize;
		this.name = name;
	}
	public Type (String name, int size, boolean variableSize, boolean isPrimary) {
		this(name, size, variableSize);
		this.isPrimaryKey = isPrimary;
	}
	public abstract String getType();
	
	public byte[] toByteArr(String value) {
		if (value.length() > size) {
			return value.substring(0, size).getBytes(Charset.forName(""));
		}
		return value.getBytes(Charset.forName("ISO-8859-1"));
	}
	
	public String fromByteArr(byte[] value) {
		return new String(value, Charset.forName("ISO-8859-1"));
	}
}
