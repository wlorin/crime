package ch.ethz.inf.dbproject.model.test;

import static ch.ethz.inf.dbproject.model.simpleDatabase.conditional.Static.*;
import static ch.ethz.inf.dbproject.model.simpleDatabase.operators.StaticOperators.*;
import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ch.ethz.inf.dbproject.model.simpleDatabase.Tuple;
import ch.ethz.inf.dbproject.model.simpleDatabase.TupleSchema;
import ch.ethz.inf.dbproject.model.simpleDatabase.operators.Case;
import ch.ethz.inf.dbproject.model.simpleDatabase.operators.Operator;
import ch.ethz.inf.dbproject.model.simpleDatabase.operators.Scan;
import ch.ethz.inf.dbproject.model.simpleDatabase.operators.Select;
import ch.ethz.inf.dbproject.model.simpleDatabase.operators.StaticOperators;
/**
 * Unit tests for Part 2.
 * @author Martin Hentschel
 */
public class Part2Test {
	
	private TupleSchema schema = TupleSchema.build().intCol("id").varcharCol("name", 20).varcharCol("status", 20).build();
	String testTable;
	File tempFile;
	
	@Before
	public void setup() throws IOException {
		tempFile = File.createTempFile("test_", "mydb");
		testTable = tempFile.getAbsolutePath();
		System.out.println(tempFile);
	}
	
	@After
	public void tearDown() {
		tempFile.deleteOnExit();
	}
	
	@Test
	public void testInsert() {
		StaticOperators.insert(testTable, schema, new String[] {"1", "asdfgäöü", "closed"});
	}
	
	@Test
	public void testDelete() {
		testInsert();
		int oldCount = StaticOperators.Count(new Scan(testTable, schema));
		StaticOperators.delete(testTable, schema, eq(col("id"), val(2)));
		assertTrue("Nothing inserted: " + oldCount, oldCount == 1);
		int afterEmptyDelete = StaticOperators.Count(new Scan(testTable, schema));
		StaticOperators.delete(testTable, schema, eq(col("id"), val(1)));
		int afterOneDelete = StaticOperators.Count(new Scan(testTable, schema));
		assertEquals("Count different after deleting nothing", oldCount, afterEmptyDelete);
		assertEquals("Didn't delete tuple", oldCount -1, afterOneDelete);
	}
	@Test
	public void testUpdate() {
		testInsert();
		int oldCount = StaticOperators.Count(new Select(new Scan(testTable, schema), eq(col("name"), val("after Update"))));
		StaticOperators.update(testTable, schema, new String[] { "name" }, new String[] {"after Update"}, eq(col("id"), val(1)));
		Scan op = new Scan(testTable, schema);
		System.out.println(concatTuples(op));
		int count = StaticOperators.Count(new Select(new Scan(testTable, schema), eq(col("name"), val("after Update"))));
		assertEquals("Didn't update tuple", 1, count);
	}
	
	@Test
	public void testAs() {
		int oldCount = StaticOperators.Count(select(new Scan(testTable, schema), eq(col("name"), val("after Update"))));
		assertTrue("No tuples", 0 == oldCount);
		testInsert();
		StaticOperators.update(testTable, schema, new String[] { "name" }, new String[] {"after Update"}, eq(col("id"), val(1)));
		int count = StaticOperators.Count(new Select(new Scan(testTable, schema).as("prefix"), eq(col("prefix.name"), val("after Update"))));
		assertTrue(count > oldCount);
	}
	
	@Test
	public void testInnerJoin() {
		testInsert();
		StaticOperators.update(testTable, schema, new String[] { "name", "id"}, new String[] {"after Update", "2"}, eq(col("id"), val(1)));
		testInsert();
		int countEq = StaticOperators.Count(new Scan(testTable, schema).as("t1").join(new Scan(testTable, schema).as("t2"), eq(col("t1.id"), col("t2.id"))));
		int countAll = StaticOperators.Count(new Scan(testTable, schema).as("t1").join(new Scan(testTable, schema).as("t2"), all()));
		assertEquals("On = ", 2, countEq);
		assertEquals("On TRUE", 4, countAll);
	}
	
	
	@Test
	public void testLeftJoin() {
		testInsert();
		StaticOperators.update(testTable, schema, new String[] { "name", "id"}, new String[] {"after Update", "2"}, eq(col("id"), val(1)));
		testInsert();
		int countEq = StaticOperators.Count(new Scan(testTable, schema).as("t1").joinLeft(new Scan(testTable, schema).as("t2"), eq(col("t1.id"), col("t2.id"))));
		int countAll = StaticOperators.Count(new Scan(testTable, schema).as("t1").joinLeft(new Scan(testTable, schema).as("t2"), all()));
		int countNone = StaticOperators.Count(new Scan(testTable, schema).as("t1").joinLeft(new Scan(testTable, schema).as("t2"), none()));
		assertEquals("On = ", 2, countEq);
		assertEquals("On TRUE", 4, countAll);
		assertEquals("On NONE", 4, countAll);
	}

	@Test
	public void testLeftRight() {
		testInsert();
		StaticOperators.update(testTable, schema, new String[] { "name", "id"}, new String[] {"after Update", "2"}, eq(col("id"), val(1)));
		testInsert();
		int countEq = StaticOperators.Count(new Scan(testTable, schema).as("t1").joinRight(new Scan(testTable, schema).as("t2"), eq(col("t1.id"), col("t2.id"))));
		int countAll = StaticOperators.Count(new Scan(testTable, schema).as("t1").joinRight(new Scan(testTable, schema).as("t2"), all()));
		int countNone = StaticOperators.Count(new Scan(testTable, schema).as("t1").joinRight(new Scan(testTable, schema).as("t2"), none()));
		assertEquals("On = ", 2, countEq);
		assertEquals("On TRUE", 4, countAll);
		assertEquals("On NONE", 4, countAll);
	}
	
	@Test
	public void testScan() {
		testInsert();
		Operator op = new Scan(testTable, schema);
		op.moveNext();
		Tuple res = op.current();
		System.out.println(res.toString());
		String expected = "id=1,name=asdfgäöü,status=closed";
		assertEquals(expected, res.toString());
	}


	@Test
	public void testSelect() {
		testInsert();
		Operator op = new Select(new Scan(testTable, schema), eq(col("status"), val("closed")));
		String expected = "id=1,name=asdfgäöü,status=closed";
		String actual = concatTuples(op);
		assertEquals(expected, actual);
	}

	@Test
	public void testProjectByName() {
		testInsert();
		Operator op = new Case(new Scan(testTable, schema), new String[]{"id", "name"});
		op.moveNext();
		Tuple t = op.current();
		assertTrue(t.getSchema().types.length == 2);
	}

	/*
	@Test
	public void testProjectByIdStatus() {
		String[] columns = new String[] { "status", "id" };
		Operator op = new Case(new Scan(new StringReader(relation), schema), columns);
		String expected = "open,1 closed,2 open,3";
		String actual = concatTuples(op);
		assertEquals(expected, actual);
	}

	@Test
	public void testSelectProject() {
		Operator op = new Case(new Select<String>(new Scan(new StringReader(relation), schema), "status", "closed"), "name");
		String expected = "Fiscal fraud";
		String actual = concatTuples(op);
		assertEquals(expected, actual);
	}
	*/

	/**
	 * Concatenates all tuples returned by the operator. The tuples are separated
	 * by a simple space.
	 * @param op operator to read from
	 * @return concatenated tuples
	 */
	private String concatTuples(Operator op) {
		StringBuilder buf = new StringBuilder();
		String separator = "";
		while (op.moveNext()) {
			buf.append(separator);
			buf.append(op.current().toString());
			separator = " ";
			
		}
		// delete last space
		//buf.deleteCharAt(buf.length() - 1);
		return buf.toString();
	}
}
