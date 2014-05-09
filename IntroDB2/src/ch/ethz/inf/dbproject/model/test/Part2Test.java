package ch.ethz.inf.dbproject.model.test;

import static org.junit.Assert.assertEquals;

import java.io.StringReader;

import org.junit.Test;

import ch.ethz.inf.dbproject.model.simpleDatabase.operators.Operator;
import ch.ethz.inf.dbproject.model.simpleDatabase.operators.Case;
import ch.ethz.inf.dbproject.model.simpleDatabase.operators.Scan;
import ch.ethz.inf.dbproject.model.simpleDatabase.operators.Select;

/**
 * Unit tests for Part 2.
 * @author Martin Hentschel
 */
public class Part2Test {

	private String[] schema = new String[] { "id", "name", "status" };
	private String relation = "1,Stolen car,open\n2,Fiscal fraud,closed\n3,High speed,open";

	@Test
	public void testScan() {
		Operator op = new Scan(new StringReader(relation), schema);
		String expected = "1,Stolen car,open 2,Fiscal fraud,closed 3,High speed,open";
		String actual = concatTuples(op);
		assertEquals(expected, actual);
	}

	@Test
	public void testSelect() {
		Operator op = new Select<String>(new Scan(new StringReader(relation), schema), "status", "closed");
		String expected = "2,Fiscal fraud,closed";
		String actual = concatTuples(op);
		assertEquals(expected, actual);
	}

	@Test
	public void testProjectByName() {
		Operator op = new Case(new Scan(new StringReader(relation), schema), "name");
		String expected = "Stolen car Fiscal fraud High speed";
		String actual = concatTuples(op);
		assertEquals(expected, actual);
	}

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
