package ch.ethz.inf.dbproject.model.simpleDatabase.operators;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import ch.ethz.inf.dbproject.model.simpleDatabase.Tuple;
import ch.ethz.inf.dbproject.model.simpleDatabase.TupleSchema;
import ch.ethz.inf.dbproject.model.simpleDatabase.TypeInt;


/**
 * The scan operator reads tuples from a file. The lines in the file contain the
 * values of a tuple. The line a comma separated.
 */
public class Scan extends Operator {

	private final TupleSchema schema;
	private final BufferedInputStream reader;
	private long myBufferPosition = 0;

	/**
	 * Contructs a new scan operator.
	 * @param fileName file to read tuples from
	 */
	
	public Scan(final String fileName, final TupleSchema schema) {
		super(schema);
		
		// create schema
		
		this.schema = schema;

		// read from file
		BufferedInputStream reader = null;
		try {
			reader = new BufferedInputStream(new FileInputStream(fileName));
		} catch (final FileNotFoundException e) {
			throw new RuntimeException("could not find file " + fileName);
		}
		this.reader = reader;
	}

	/**
	 * Constructs a new scan operator (mainly for testing purposes).
	 * @param reader reader to read lines from
	 * @param columns column names
	 */

	@Override
	public boolean moveNext() {
		/* the first bit signalises whether the row is deleted. 
		 * The folowing [schema.types.length] bits signal whether the field is NULL
		 * the following 8 - (schema.types.length % 8 + 1) bits are disregarded
		 */
		
		try {
			int flags[] = new int[schema.types.length / 8 + 1];
			for (int i = 0; i < flags.length; i++) {
				flags[i] = reader.read();
				myBufferPosition++;
				if (flags[i] == -1) {
					reader.close();
					return false;
				}
			}
			
			String[] values = new String[schema.types.length];
			TypeInt intConv = new TypeInt();
			for (int i = 0; i < schema.types.length; i++) {
				int size = 0;
				if (schema.types[i].variableSize) {
					byte[] sizeByte = new byte[4];
					reader.read(sizeByte);
					myBufferPosition += sizeByte.length;
					size = intConv.getIntFromByteArr(sizeByte);
				}
				else {
					size = schema.types[i].size;
				}
				
				int index = (i + 1) / 8;
				int mask = 0b10000000 >> ((i + 1) % 8);
				if ((flags[index] & mask) > 1) {
					values[i] = null;
				}
				else if (size > 0) {
					byte[] buf = new byte[size];
					reader.read(buf);
					myBufferPosition += buf.length;
					values[i] = schema.types[i].fromByteArr(buf);
				}
			}
			if (flags[0] >> 7 == 1) { //record is deleted
				return moveNext();
			}
			this.current = new Tuple(schema, values);
			return true;
			
		} catch (final IOException e) {
			throw new RuntimeException("could not read: " + this.reader + 
				". Error is " + e);
		}
		
	}
	public long getBufferPosition() {
		return myBufferPosition;
	}

}
