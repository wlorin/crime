package ch.ethz.inf.dbproject.model.simpleDatabase.operators;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import ch.ethz.inf.dbproject.model.simpleDatabase.TupleSchema;


/**
 * The scan operator reads tuples from a file. The lines in the file contain the
 * values of a tuple. The line a comma separated.
 */
public class Scan extends Operator {

	private final TupleSchema schema;
	private final BufferedReader reader;

	/**
	 * Contructs a new scan operator.
	 * @param fileName file to read tuples from
	 */
	public Scan(
		final String fileName, 
		final String[] columnNames
	) {
		
		// create schema
		this.schema = new TupleSchema(columnNames);

		// read from file
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(fileName));
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
	public Scan(
		final Reader reader, 
		final String[] columnNames
	) {
		this.reader = new BufferedReader(reader);
		this.schema = new TupleSchema(columnNames);
	}

	@Override
	public boolean moveNext() {
		
		try {
			
			// TODO
			// a) read next line
			// b) check if we are at the end of the file (line would be null)
			//   b1. if we reached end of the file, close the buffered reader
			// c) split up comma separated values
			// d) create new tuple using schema and values
			throw new IOException("NOT IMPLEMENTED");
			
		} catch (final IOException e) {
			
			throw new RuntimeException("could not read: " + this.reader + 
				". Error is " + e);
			
		}
		
	}

}
