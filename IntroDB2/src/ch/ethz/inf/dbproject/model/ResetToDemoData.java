package ch.ethz.inf.dbproject.model;
import static ch.ethz.inf.dbproject.model.simpleDatabase.conditional.Static.col;
import static ch.ethz.inf.dbproject.model.simpleDatabase.conditional.Static.eq;
import static ch.ethz.inf.dbproject.model.simpleDatabase.conditional.Static.val;
import static ch.ethz.inf.dbproject.model.simpleDatabase.operators.StaticOperators.insert;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;




import ch.ethz.inf.dbproject.model.simpleDatabase.TupleSchema;
import ch.ethz.inf.dbproject.model.simpleDatabase.operators.Scan;
import ch.ethz.inf.dbproject.model.simpleDatabase.operators.Select;

public class ResetToDemoData {
	
	private DatastoreInterfaceSimpleDatabase intf = new DatastoreInterfaceSimpleDatabase();

	public void resetToDemoData() {
		List<String> files = new ArrayList<String>();
		files.add(intf.getTableName(Case.class));
		files.add(intf.getTableName(CaseNote.class));
		files.add(intf.getTableName(Conviction.class));
		files.add(intf.getTableName(Crime.class));
		files.add(intf.getTableName(PoINote.class));
		files.add(intf.getTableName(PoI.class));
		files.add(intf.getTableName(Suspect.class));
		files.add(intf.getTableName(User.class));
		for (String f : files) {
			File file = new File(f);
			if (file.exists()) {
				file.delete();
			}
		}
		insertCrime();
		insertUser();
		insertPoI();
		insertCase();
		insertCaseNote();
		insertConviction();
		insertPoINote();
		insertSuspect();
		
	}
	
	private void insertUser() {
		String fileName = intf.getTableName(User.class);
		TupleSchema schema = intf.getSchema(User.class);
		String pwmd5 = "";
		try {
			pwmd5 = MessageDigest.getInstance("MD5").digest("password".getBytes()).toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		insert(fileName, schema, new String[] {null, "Max", pwmd5});
		insert(fileName, schema, new String[] {null, "Philipp", pwmd5});
		insert(fileName, schema, new String[] {null, "Anna", pwmd5});
		insert(fileName, schema, new String[] {null, "Christian", pwmd5});
		insert(fileName, schema, new String[] {null, "Lorin", pwmd5});
		insert(fileName, schema, new String[] {null, "Nina", pwmd5});
	}

	private void insertSuspect() {
		String fileName = intf.getTableName(Suspect.class);
		TupleSchema schema = intf.getSchema(Suspect.class);
		insert(fileName, schema, new String[] { getPoIIdByName("Urs Gerold"), String.valueOf(intf.getCrimeIdByName("Case 5"))});
		
	}

	private void insertPoI() {
		String fileName = intf.getTableName(PoI.class);
		TupleSchema schema = intf.getSchema(PoI.class);
		insert(fileName, schema, new String[] { null, "Sarah Meier", "1980-08-25", getUserIdByName("Max") });
		insert(fileName, schema, new String[] { null, "Hans Müller", "1987-07-27", getUserIdByName("Max") });
		insert(fileName, schema, new String[] { null, "Oliver Sieber", "1986-06-26", getUserIdByName("Christian") });
		insert(fileName, schema, new String[] { null, "Karl Hinrichsen", "1985-05-25", getUserIdByName("Christian") });
		insert(fileName, schema, new String[] { null, "Fabienne Neuwirth", "1984-04-24", getUserIdByName("Anna") });
		insert(fileName, schema, new String[] { null, "Peter Müller", "1983-03-23", getUserIdByName("Anna") });
		insert(fileName, schema, new String[] { null, "Urs Gerold", "1982-03-22", getUserIdByName("Anna") });
		insert(fileName, schema, new String[] { null, "Tamara Jäger", "1981-02-22", getUserIdByName("Anna") });
		insert(fileName, schema, new String[] { null, "Lorin Weilenmann", "1984-08-05", getUserIdByName("Lorin") });
		insert(fileName, schema, new String[] { null, "Michael Peters", "1972-12-16", getUserIdByName("Lorin") });
		insert(fileName, schema, new String[] { null, "Stefan Brunner", "1970-08-25", getUserIdByName("Lorin") });
		insert(fileName, schema, new String[] { null, "Lukas Casanova", "1979-02-22", getUserIdByName("Nina") });
		
	}

	private void insertPoINote() {
		String fileName = intf.getTableName(PoINote.class);
		TupleSchema schema = intf.getSchema(PoINote.class);
		insert(fileName, schema, new String[] { null, getPoIIdByName("Tamara Jäger"), "2014-03-22 18:25:12", "Kein Alibi für Tatzeit", getUserIdByName("Lorin") });
	}

	private void insertConviction() {
		String fileName = intf.getTableName(Conviction.class);
		TupleSchema schema = intf.getSchema(Conviction.class);
		insert(fileName, schema, new String[] {
				getPoIIdByName("Michael Peters"),
				getCaseIdByName("Case 7"),
				String.valueOf(intf.getCrimeIdByName("Einbruch")),
				"2014-04-11",
				"Geldstrafe 500 Franken"
		});
		insert(fileName, schema, new String[] {
				getPoIIdByName("Oliver Sieber"),
				getCaseIdByName("Case 6"),
				String.valueOf(intf.getCrimeIdByName("Körperverletzung")),
				"2014-05-11",
				"3 Jahre Gefängnis"
		});
	}
	
	private String getCaseIdByName(final String name) {
		Scan scan = new Scan(intf.getTableName(Case.class), intf.getSchema(Case.class));
		Select select = new Select(scan, eq(col("Name"), val(name)));
		if (select.moveNext()) {
			return select.current().getString("CaseId");
		}
		throw new RuntimeException("couldn't find caseid");
	}
	
	private String getUserIdByName(final String name) {
		Scan scan = new Scan(intf.getTableName(User.class), intf.getSchema(User.class));
		Select select = new Select(scan, eq(col("Name"), val(name)));
		if (select.moveNext()) {
			return select.current().getString("UserId");
		}
		throw new RuntimeException("couldn't find userid");
	}
	private String getPoIIdByName(final String name) {
		Scan scan = new Scan(intf.getTableName(PoI.class), intf.getSchema(PoI.class));
		Select select = new Select(scan, eq(col("Name"), val(name)));
		if (select.moveNext()) {
			return select.current().getString("PoIId");
		}
		throw new RuntimeException("couldn't find poiid");
	}

	private void insertCaseNote() {
		String fileName = intf.getTableName(CaseNote.class);
		TupleSchema schema = intf.getSchema(CaseNote.class);
		insert(fileName, schema, new String[] { 
				null, 
				getCaseIdByName("Mord 3"), 
				"2014-03-02 17:15:12",
				"Mord durch Schusswaffe, Opfer: Mann, 28 Jahre",
				getUserIdByName("Max")}
		);
		insert(fileName, schema, new String[] { 
				null, 
				getCaseIdByName("Mord 3"), 
				"2014-02-05 11:11:52",
				"bisher keine Augenzeugen gefunden",
				getUserIdByName("Christian")}
		);
		insert(fileName, schema, new String[] { 
				null, 
				getCaseIdByName("Diebstahl 1"), 
				"2014-03-01 13:12:52",
				"Rucksack gestohlen",
				getUserIdByName("Anna")}
		);
		insert(fileName, schema, new String[] { 
				null, 
				getCaseIdByName("Diebstahl 2"), 
				"2014-05-05 23:22:52",
				"durch Balkontüre ins Haus eingebrochen, Bargeld und Schmuck gestohlen",
				getUserIdByName("Christian")}
		);
		insert(fileName, schema, new String[] { 
				null, 
				getCaseIdByName("Bankraub 2"), 
				"2014-04-19 12:56:00",
				"Täter gefasst",
				getUserIdByName("Anna")}
		);
		
	}

	private void insertCrime() {
		String fileName = intf.getTableName(Crime.class);
		TupleSchema schema = intf.getSchema(Crime.class);
		insert(fileName, schema, new String[] { null, "Körperverletzung"});
		insert(fileName, schema, new String[] { null, "Mord"});
		insert(fileName, schema, new String[] { null, "Diebstahl"});
		insert(fileName, schema, new String[] { null, "Einbruch"});
		insert(fileName, schema, new String[] { null, "Bankraub"});
		
	}
	private void insertCase() {
		String fileName = intf.getTableName(Case.class);
		TupleSchema schema = intf.getSchema(Case.class);
		insert(fileName, schema, new String[] {
				null,
				"Diebstahl 1",
				String.valueOf(intf.getCrimeIdByName("Diebstahl")),
				"open",
				"2014-03-11",
				"12:31:00",
				"Zürich"
		});
		insert(fileName, schema, new String[] {
				null,
				"Diebstahl 2",
				String.valueOf(intf.getCrimeIdByName("Diebstahl")),
				"open",
				"2014-01-13",
				"09:15:00",
				"Rapperswil"
		});
		insert(fileName, schema, new String[] {
				null,
				"Mord in Basel",
				String.valueOf(intf.getCrimeIdByName("Mord")),
				"open",
				"2014-03-11",
				"09:20:00",
				"Basel"
		});
		insert(fileName, schema, new String[] {
				null,
				"Mord in Zürich",
				String.valueOf(intf.getCrimeIdByName("Mord")),
				"open",
				"2014-04-11",
				"11:23:00",
				"Zürich"
		});
		insert(fileName, schema, new String[] {
				null,
				"Mord 3",
				String.valueOf(intf.getCrimeIdByName("Mord")),
				"open",
				"2014-02-17",
				"14:43:00",
				"Zürich"
		});
		insert(fileName, schema, new String[] {
				null,
				"Schlägerei",
				String.valueOf(intf.getCrimeIdByName("Körperverletzung")),
				"closed",
				"2014-01-01",
				"00:43:00",
				"Aarau"
		});
		insert(fileName, schema, new String[] {
				null,
				"Bankraub 1",
				String.valueOf(intf.getCrimeIdByName("Bankraub")),
				"closed",
				"2014-02-09",
				"07:43:00",
				"Zürich"
		});
		insert(fileName, schema, new String[] {
				null,
				"Bankraub 2",
				String.valueOf(intf.getCrimeIdByName("Einbruch")),
				"open",
				"2013-12-09",
				"03:12:00",
				"Zürich"
		});
		insert(fileName, schema, new String[] {
				null,
				"Case 5",
				String.valueOf(intf.getCrimeIdByName("Einbruch")),
				"open",
				"2013-12-05",
				"03:15:00",
				"Bern"
		});
		insert(fileName, schema, new String[] {
				null,
				"Case 6",
				String.valueOf(intf.getCrimeIdByName("Einbruch")),
				"open",
				"2013-12-06",
				"02:06:00",
				"Solothirn"
		});
		insert(fileName, schema, new String[] {
				null,
				"Case 7",
				String.valueOf(intf.getCrimeIdByName("Einbruch")),
				"open",
				"2013-12-07",
				"03:17:00",
				"Luzern"
		});
		insert(fileName, schema, new String[] {
				null,
				"Case 8",
				String.valueOf(intf.getCrimeIdByName("Einbruch")),
				"open",
				"2013-12-08",
				"03:18:00",
				"Affoltern a.A."
		});
		insert(fileName, schema, new String[] {
				null,
				"Case 9",
				String.valueOf(intf.getCrimeIdByName("Einbruch")),
				"open",
				"2013-12-09",
				"03:19:00",
				"was weiss ich"
		});
	}
}
