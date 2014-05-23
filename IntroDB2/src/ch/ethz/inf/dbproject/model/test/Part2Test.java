package ch.ethz.inf.dbproject.model.test;

import static ch.ethz.inf.dbproject.model.simpleDatabase.conditional.Static.all;
import static ch.ethz.inf.dbproject.model.simpleDatabase.conditional.Static.col;
import static ch.ethz.inf.dbproject.model.simpleDatabase.conditional.Static.eq;
import static ch.ethz.inf.dbproject.model.simpleDatabase.conditional.Static.none;
import static ch.ethz.inf.dbproject.model.simpleDatabase.conditional.Static.or;
import static ch.ethz.inf.dbproject.model.simpleDatabase.conditional.Static.val;
import static ch.ethz.inf.dbproject.model.simpleDatabase.operators.StaticOperators.select;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ch.ethz.inf.dbproject.model.DatastoreInterfaceSimpleDatabase;
import ch.ethz.inf.dbproject.model.User;
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
	
	private TupleSchema schema = TupleSchema.build().intCol("id").asPrimary().asAutoIncrement().varcharCol("name", 20).varcharCol("status", 20).build();
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
		int oldCount = StaticOperators.count(new Scan(testTable, schema));
		StaticOperators.delete(testTable, schema, eq(col("id"), val(2)));
		assertTrue("Nothing inserted: " + oldCount, oldCount == 1);
		int afterEmptyDelete = StaticOperators.count(new Scan(testTable, schema));
		StaticOperators.delete(testTable, schema, eq(col("id"), val(1)));
		int afterOneDelete = StaticOperators.count(new Scan(testTable, schema));
		assertEquals("Count different after deleting nothing", oldCount, afterEmptyDelete);
		assertEquals("Didn't delete tuple", oldCount -1, afterOneDelete);
	}
	@Test
	public void testUpdate() {
		testInsert();
		int oldCount = StaticOperators.count(new Select(new Scan(testTable, schema), eq(col("name"), val("after Update"))));
		StaticOperators.update(testTable, schema, new String[] { "name" }, new String[] {"after Update"}, eq(col("id"), val(1)));
		Scan op = new Scan(testTable, schema);
		System.out.println(concatTuples(op));
		int count = StaticOperators.count(new Select(new Scan(testTable, schema), eq(col("name"), val("after Update"))));
		assertEquals("Didn't update tuple", 1, count);
	}
	
	@Test
	public void testAs() {
		int oldCount = StaticOperators.count(select(new Scan(testTable, schema), eq(col("name"), val("after Update"))));
		assertTrue("No tuples", 0 == oldCount);
		testInsert();
		StaticOperators.update(testTable, schema, new String[] { "name" }, new String[] {"after Update"}, eq(col("id"), val(1)));
		int count = StaticOperators.count(new Select(new Scan(testTable, schema).as("prefix"), eq(col("prefix.name"), val("after Update"))));
		assertTrue(count > oldCount);
	}
	
	@Test
	public void testInnerJoin() {
		testInsert();
		StaticOperators.update(testTable, schema, new String[] { "name", "id"}, new String[] {"after Update", "2"}, eq(col("id"), val(1)));
		testInsert();
		int countEq = StaticOperators.count(new Scan(testTable, schema).as("t1").join(new Scan(testTable, schema).as("t2"), eq(col("t1.id"), col("t2.id"))));
		int countAll = StaticOperators.count(new Scan(testTable, schema).as("t1").join(new Scan(testTable, schema).as("t2"), all()));
		assertEquals("On = ", 2, countEq);
		assertEquals("On TRUE", 4, countAll);
	}
	
	
	@Test
	public void testLeftJoin() {
		testInsert();
		StaticOperators.update(testTable, schema, new String[] { "name", "id"}, new String[] {"after Update", "2"}, eq(col("id"), val(1)));
		testInsert();
		int countEq = StaticOperators.count(new Scan(testTable, schema).as("t1").joinLeft(new Scan(testTable, schema).as("t2"), eq(col("t1.id"), col("t2.id"))));
		int countAll = StaticOperators.count(new Scan(testTable, schema).as("t1").joinLeft(new Scan(testTable, schema).as("t2"), all()));
		int countNone = StaticOperators.count(new Scan(testTable, schema).as("t1").joinLeft(new Scan(testTable, schema).as("t2"), none()));
		assertEquals("On = ", 2, countEq);
		assertEquals("On TRUE", 4, countAll);
		assertEquals("On NONE", 4, countAll);
	}

	@Test
	public void testLeftRight() {
		testInsert();
		StaticOperators.update(testTable, schema, new String[] { "name", "id"}, new String[] {"after Update", "2"}, eq(col("id"), val(1)));
		testInsert();
		int countEq = StaticOperators.count(new Scan(testTable, schema).as("t1").joinRight(new Scan(testTable, schema).as("t2"), eq(col("t1.id"), col("t2.id"))));
		int countAll = StaticOperators.count(new Scan(testTable, schema).as("t1").joinRight(new Scan(testTable, schema).as("t2"), all()));
		int countNone = StaticOperators.count(new Scan(testTable, schema).as("t1").joinRight(new Scan(testTable, schema).as("t2"), none()));
		assertEquals("On = ", 2, countEq);
		assertEquals("On TRUE", 4, countAll);
		assertEquals("On NONE", 4, countAll);
	}
	
	@Test
	public void testScan() {
		String[] values = new String[] { "1", "test", "Test2" };
		StaticOperators.insert(testTable, schema, values);
		Operator op = new Scan(testTable, schema);
		op.moveNext();
		Tuple res = op.current();
		System.out.println(res.toString());
		String expected = "id=1,name=test,status=Test2";
		assertEquals(expected, res.toString());
	}
	@Test
	public void testScanWithNull() {
		String[] values = new String[] { "1", null, "Test2" };
		StaticOperators.insert(testTable, schema, values);
		Operator op = new Scan(testTable, schema);
		op.moveNext();
		Tuple res = op.current();
		System.out.println(res.toString());
		String expected = "id=1,name=null,status=Test2";
		assertEquals(expected, res.toString());
		assertTrue("result must be null", res.isNull(1));
	}
	@Test
	public void testAutoIncrement() {
		StaticOperators.insert(testTable, schema, new String[] { null, "1", "1" });
		StaticOperators.insert(testTable, schema, new String[] { null, "2", "2" });
		StaticOperators.insert(testTable, schema, new String[] { "3", "3", "3" });
		StaticOperators.insert(testTable, schema, new String[] { null, "4", "4" });
		Scan scan = new Scan(testTable, schema);
		scan.moveNext();
		assertEquals(scan.current().toString(), "id=1,name=1,status=1");
		scan.moveNext();
		assertEquals(scan.current().toString(), "id=2,name=2,status=2");
		scan.moveNext();
		assertEquals(scan.current().toString(), "id=3,name=3,status=3");
		scan.moveNext();
		assertEquals(scan.current().toString(), "id=4,name=4,status=4");
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
	@Test
	public void testGetById() {
		DatastoreInterfaceSimpleDatabase intf = new DatastoreInterfaceSimpleDatabase();
		Class<User> clazz = User.class;
		TupleSchema schema = intf.getSchema(clazz);
		String tableName = intf.getTableName(clazz);
		String idCol = intf.getIdColName(clazz);
		int id1 = StaticOperators.insert(tableName, schema, new String[] { null, "TestUser1", "pwd"});
		int id2 = StaticOperators.insert(tableName, schema, new String[] { null, "TestUser" + (id1 + 1), "pwd"});
		int id3 = StaticOperators.insert(tableName, schema, new String[] { null, "TestUser3", "pwd"});
		assertTrue("id must be > 0", id2 > 0);
		User user = intf.getById(id2, clazz);
		List<Tuple> del = StaticOperators.delete(tableName, schema, eq(col(idCol), val(id2)));
		assertTrue("must have deleted exactly one row", del.size() == 1);
		String expected = "UserId=" + id2 + ",Name=TestUser" + id2 + ",Password=pwd";
		String result = "UserId=" + user.getUserid() + ",Name=" + user.getName() + ",Password=" + user.getPassword();
		assertEquals(expected, result);
		assertEquals(result, del.get(0).toString());
		StaticOperators.delete(tableName,  schema, or(eq(col(idCol), val(id1)), eq(col(idCol), val(id3))));
	}
	
	@Test
	public void TestKeyRestriction() {
		boolean success = false;
		testInsert();
		try {
			testInsert();
		} catch (RuntimeException e) {
			success = true;
		}
		assertTrue(success);
	}
	
	@Test
	public void TestTableOptimiser() {
		List<Integer> ids = new ArrayList<Integer>();
		for (int i = 0; i < 100; i++) {
			int id =StaticOperators.insert(testTable, schema, new String[] { null, "name" + i, "status" });
			if (id % 2 == 0) {
				ids.add(id);
			}
		}
		Long oldsize = tempFile.length();
		int delCount = 0;
		for (Integer i : ids) {
			delCount++;
			List<Tuple> res = StaticOperators.delete(testTable, schema, eq(col("id"), val(i)));
			assertTrue("expect one delete for id " + i, res.size() == 1);
		}
		//this scan should now trigger the optimiser
		int count = StaticOperators.count(new Scan(testTable, schema));
		System.out.println("cleaned up " + (oldsize - tempFile.length()) + ", about " + ((float)tempFile.length() * 100 / oldsize) + "%");
		assertTrue("delCount should be 50", delCount == 50);
		assertTrue("expected 50 results, found " + count, count == 50);
		assertTrue(tempFile.length() < oldsize);
	}
	

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
