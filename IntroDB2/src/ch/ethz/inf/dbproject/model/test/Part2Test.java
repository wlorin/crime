package ch.ethz.inf.dbproject.model.test;

import static org.junit.Assert.assertEquals;

import java.io.StringReader;

import org.junit.Test;

import ch.ethz.inf.dbproject.model.simpleDatabase.Tuple;
import ch.ethz.inf.dbproject.model.simpleDatabase.TupleSchema;
import ch.ethz.inf.dbproject.model.simpleDatabase.Type;
import ch.ethz.inf.dbproject.model.simpleDatabase.TypeInt;
import ch.ethz.inf.dbproject.model.simpleDatabase.TypeVarChar;
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
	
	private TupleSchema schema = new TupleSchema(new Type[] {new TypeInt("id"), new TypeVarChar("name", 20), new TypeVarChar("status", 20)});
	
	@Test
	public void testInsert() {
		StaticOperators.insert("test", schema, new String[] {"1", "asdfgäöü", "closed"});
	}
	
	@Test
	public void testDelete() {
		testInsert();
		int oldCount = StaticOperators.Count(new Scan("test", schema));
		StaticOperators.delete("test", schema, "id", "2");
		int afterEmptyDelete = StaticOperators.Count(new Scan("test", schema));
		StaticOperators.delete("test", schema, "id", "1");
		int afterOneDelete = StaticOperators.Count(new Scan("test", schema));
		assert(oldCount == afterEmptyDelete && oldCount -1 == afterOneDelete);
	}
	@Test
	public void testUpdate() {
		int oldCount = StaticOperators.Count(new Select(new Scan("test", schema), "name", "after Update"));
		testInsert();
		StaticOperators.update("test", schema, new String[] { "name",  "id" }, new String[] {"after Update", "1"});
		int count = StaticOperators.Count(new Select(new Scan("test", schema), "name", "after Update"));
		assert(count > oldCount);
	}
	
	@Test
	public void testScan() {
		Operator op = new Scan("test", schema);
		op.moveNext();
		Tuple res = op.current();
		System.out.println(res.toString());
		String expected = "id=1,name=asdfgäöü,status=ksajdfljasdflk";
		assertEquals(expected, res.toString());
	}


	@Test
	public void testSelect() {
		Operator op = new Select(new Scan("test", schema), "status", "closed");
		String expected = "2,Fiscal fraud,closed";
		String actual = concatTuples(op);
		assertEquals(expected, actual);
	}

	@Test
	public void testProjectByName() {
		Operator op = new Case(new Scan("test", schema), new String[]{"id", "name"});
		op.moveNext();
		Tuple t = op.current();
		assert(t.getSchema().types.length == 2);
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
		while (op.moveNext()) {
			buf.append(op.current().toString());
			buf.append(" ");
		}
		// delete last space
		//buf.deleteCharAt(buf.length() - 1);
		return buf.toString();
	}
}
