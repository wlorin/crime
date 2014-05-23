package ch.ethz.inf.dbproject.model;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import ch.ethz.inf.dbproject.model.meta.Entity;
import ch.ethz.inf.dbproject.model.meta.TableName;
import ch.ethz.inf.dbproject.model.simpleDatabase.Tuple;
import ch.ethz.inf.dbproject.model.simpleDatabase.TupleSchema;
import ch.ethz.inf.dbproject.model.simpleDatabase.operators.Operator;
import ch.ethz.inf.dbproject.model.simpleDatabase.operators.Scan;
import ch.ethz.inf.dbproject.model.simpleDatabase.operators.Select;
import ch.ethz.inf.dbproject.model.simpleDatabase.operators.Sort;
import ch.ethz.inf.dbproject.model.simpleDatabase.operators.StaticOperators;
import static ch.ethz.inf.dbproject.model.simpleDatabase.conditional.Static.*;


public final class DatastoreInterfaceSimpleDatabase implements DatastoreInterface {

	/*
	 * Files
	 */
	String caseFile = "caseFile";
	String userFile = "userFile";
	String convictedFile = "convictedFile";
	String crimeFile = "crimeFile";
	String poiFile = "poiFile";
	String suspectFile = "suspectFile";
	
	
	/*
	 * Schema
	 */
	private final HashMap<String, TupleSchema> schemas;
	
	public DatastoreInterfaceSimpleDatabase() {
		schemas = new HashMap<String, TupleSchema>();
		schemas.put(getTableName(Case.class), TupleSchema.build()
			.intCol("CaseId").asPrimary().asAutoIncrement()
			.varcharCol("Name", 100)
			.intCol("CrimeId")
			.varcharCol("Status", 6)
			.dateCol("Date")
			.timeCol("Time")
			.varcharCol("Location", 50)
			.build());
		schemas.put(getTableName(CaseNote.class), TupleSchema.build()
			.intCol("CaseNoteId").asPrimary().asAutoIncrement()
			.intCol("CaseId")
			.timestampCol("Timestamp")
			.varcharCol("Note", 4096)
			.intCol("UserId")
			.build());
		
		schemas.put(getTableName(Conviction.class), TupleSchema.build()
			.intCol("PoIId")
			.intCol("CaseId")
			.intCol("CrimeId")
			.dateCol("Date")
			.varcharCol("Sentence", 50)
			.markPrimary("PoIId", "CaseId", "CrimeId")
			.build());
		
		schemas.put(getTableName(Crime.class), TupleSchema.build()
			.intCol("CrimeId").asPrimary().asAutoIncrement()
			.varcharCol("Crime", 50)
			.build());
		
		schemas.put(getTableName(PoINote.class), TupleSchema.build()
			.intCol("PoINoteId").asPrimary().asAutoIncrement()
			.intCol("PoIId")
			.timestampCol("Timestamp")
			.varcharCol("Note", 4096)
			.intCol("UserId")
			.build());
		
		schemas.put(getTableName(Suspect.class), TupleSchema.build()
			.intCol("PoIId")
			.intCol("CaseId")
			.markPrimary("PoIId", "CaseId")
			.build());
		
		schemas.put(getTableName(User.class), TupleSchema.build()
			.intCol("UserId").asPrimary().asAutoIncrement()
			.varcharCol("Name", 50)
			.varcharCol("Password", 32)
			.build());
		
		schemas.put(getTableName(PoI.class), TupleSchema.build()
			.intCol("PoiId").asPrimary().asAutoIncrement()
			.varcharCol("Name", 50)
			.dateCol("Birthdate")
			.intCol("UserId")
			.build());
	}
	
	public <T extends Entity> TupleSchema getSchema(Class<T> clazz) {
		return schemas.get(clazz.getSimpleName());
	}
	public <T extends Entity> String getTableName(Class<T> clazz) {
		return getRawTableName(clazz);
	}
	
	public <T extends Entity> String getIdColName(Class<T> clazz) {
		return getRawTableName(clazz) + "Id";
	}
	
	private <T extends Entity> String getRawTableName(Class<T> clazz) {
		TableName tableAnnotation = clazz.getAnnotation(TableName.class);
		if (tableAnnotation != null) {
			return tableAnnotation.value();
		}
		else {
			return clazz.getSimpleName();
		}
	}
	
	@Override
	public final <T extends Entity> List<T> getAll(Class<T> clazz) {
		String tableName = getTableName(clazz);
		Scan scan = new Scan(tableName, getSchema(clazz));
		return all(scan, clazz);
	}
	
	private <T extends Entity> List<T> all(Operator op, Class<T> clazz) {
		try {
			Constructor<T> constructor = clazz.getConstructor(Tuple.class);
			List<T> ts = new ArrayList<T>();
			while (op.moveNext()) {
				ts.add(constructor.newInstance(op.current()));
			}
			return ts;
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public <T extends Entity> T getById(Long id, Class<T> clazz) {
		return getById(id.intValue(), clazz);
	}
	public final <T extends Entity> T getById(final Integer id, Class<T> clazz) {
		if (id == null) {
			return null;
		}
		String idColName = getIdColName(clazz);
		Scan scan = new Scan(getTableName(clazz), getSchema(clazz));
		Select select = new Select(scan, eq(col(idColName), val(id)));
		List<T> all = all(select, clazz);
		if (all.size() == 1) {
			return all.get(0);
		}
		
		if (all.size() > 1) {
			throw new IllegalStateException("Expected at most one result for this select: column=" + idColName + ", value=" + id);
		}
		
		return null;
	}

	@Override
	public List<Case> getByStatus(String status) {
		final Scan scan = new Scan(caseFile, getSchema(Case.class));	
		final Select select = new Select(scan, eq(col("Status"), val(status)));
		List<Case> cases = new ArrayList<Case>();
		while (select.moveNext()){
			final Tuple tuple = select.current();					
			Case c = new Case(
				tuple.getInt(0),
				tuple.get(1),
				tuple.getInt(2),
				tuple.get(3),
				tuple.get(4),
				tuple.getDate(5),
				tuple.getTime(6)
				);
			cases.add(c);
		}
		return cases;
	}

	@Override
	public List<Case> getMostRecentCases(int number) {
		final Scan scan = new Scan(caseFile, getSchema(Case.class));	
		final Select select = new Select(scan, eq(col("Status"), val("open"))); 
		final Sort sort = new Sort(select, "Date", true);
		List<Case> cases = new ArrayList<Case>();
		for (int i = 0; i < number; i++) {
			if (sort.moveNext()) {			
				final Tuple tuple = sort.current();
				Case c = new Case(
						tuple.getInt(0),
						tuple.get(1),
						tuple.getInt(2),
						tuple.get(3),
						tuple.get(4),
						tuple.getDate(5),
						tuple.getTime(6)
						);
				cases.add(c);
			}
		}
		return cases;
	}

	@Override
	public List<Case> getOldestUnsolvedCases(int number) {		
		final Scan scan = new Scan(caseFile, getSchema(Case.class));	
		final Select select = new Select(scan, eq(col("Status"), val("open"))); 
		final Sort sort = new Sort(select, "Date", false);
		List<Case> cases = new ArrayList<Case>();
		for (int i = 0; i < number; i++) {
			if (sort.moveNext()) {			
				final Tuple tuple = sort.current();
				Case c = new Case(
						tuple.getInt(0),
						tuple.get(1),
						tuple.getInt(2),
						tuple.get(3),
						tuple.get(4),
						tuple.getDate(5),
						tuple.getTime(6)
						);
				cases.add(c);
			}
		}
		return cases;
	}

	/**
	 * Will return null if users with given credentials does not exist.
	 * @param number
	 * @return
	 */
	@Override
	public User tryGetUserFromCredentials(String name, String password) {
		final Scan scan = new Scan(userFile, getSchema(User.class));
		final Select select = new Select(scan, and(eq(col("Name"), val(name)), eq(col("Password"), val(password))));
		User user = null;
		if (select.moveNext()) {
			Tuple tuple = select.current();
			user = new User(tuple.getInt(0), name, password);
		}
		return user;
	}

	@Override
	public void removeSuspect(int caseId, int poiId) {
		StaticOperators.delete(suspectFile, getSchema(Suspect.class), and(eq(col("CaseId"), val(caseId)), eq(col("PoIId"), val(poiId))));		
	}

	@Override
	public void deleteCase(int caseId) {
		StaticOperators.delete(caseFile, getSchema(Case.class), eq(col("CaseId"), val(caseId)));
		
	}

	@Override
	public void deletePoI(int poiId) {
		StaticOperators.delete(poiFile, getSchema(PoI.class), eq(col("PoIId"), val(poiId)));
		
	}

	@Override
	public User insertUser(String name, String password) {
		StaticOperators.insert(userFile, getSchema(User.class), new String[] {null, name, password});
		Scan scan = new Scan(userFile, getSchema(User.class));
		Select select = new Select(scan, and(eq(col("Name"), val(name)), eq(col("Password"), val(password))));
		if (select.moveNext()) {
			Tuple tuple = select.current();
			return new User(tuple.getInt(0), name, password);
		}
		return null;
	}

	@Override
	public Suspect insertSuspect(int caseId, int poiId) {
		StaticOperators.insert(suspectFile, getSchema(Suspect.class), new String[] {Integer.toString(caseId), Integer.toString(poiId)});

		return new Suspect(caseId, poiId);
	}

	@Override
	public CaseNote insertComment(String comment, int caseId, int userid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	// TODO return Crime
	public Crime insertCrime(String crimeName) {
		StaticOperators.insert(crimeFile, getSchema(Crime.class), new String[] {null, crimeName});
		return null;
	}

	@Override
	public PoI insertPoI(String name, Date birthdate) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String birth = format.format(birthdate);
		StaticOperators.insert(poiFile, getSchema(PoI.class), new String[] {null, name, birth , null});
		return null;
	}

	@Override
	public PoI updatePoI(Long id, String name, Date birthdate) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PoINote insertPoINote(String comment, int poiId, int userid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCrimeById(int id) {
		Scan scan = new Scan(crimeFile, getSchema(Crime.class));
		Select select = new Select(scan, eq(col("CrimeId"), val(id)));
		if (select.moveNext()) {
			Tuple tuple = select.current();
			return tuple.get(1);
		}
		return null;
	}

	@Override
	public String getCasenameById(int id) {
		Scan scan = new Scan(caseFile, getSchema(Case.class));
		Select select = new Select(scan, eq(col("CaseId"), val(id)));
		if (select.moveNext()) {
			Tuple tuple = select.current();
			return tuple.get(1);
		}
		return null;
	}

	public int getCrimeIdByName(String name) {
		Scan scan = new Scan(crimeFile, getSchema(Crime.class));
		Select select = new Select(scan, eq(col("Crime"), val(name)));
		if (select.moveNext()) {
			return select.current().getInt(0);
		}
		return -1;
	}
	
	@Override
	public List<Case> getProjectsByCategory(String category) {
		int id = getCrimeIdByName(category);
		final Scan scan = new Scan(caseFile, getSchema(Case.class));	
		final Select select = new Select(scan, eq(col("CrimeId"), val(id))); 
		List<Case> cases = new ArrayList<Case>();
		while (select.moveNext()) {			
			final Tuple tuple = select.current();
			Case c = new Case(
					tuple.getInt(0),
					tuple.get(1),
					tuple.getInt(2),
					tuple.get(3),
					tuple.get(4),
					tuple.getDate(5),
					tuple.getTime(6)
					);
				cases.add(c);
			}
		return cases;
	}

	@Override
	public List<Case> getProjectsWithoutCategory() {
		final Scan scan = new Scan(caseFile, getSchema(Case.class));	
		final Select select = new Select(scan, eq(col("CrimeId"), val(null))); 
		List<Case> cases = new ArrayList<Case>();
		while (select.moveNext()) {			
			final Tuple tuple = select.current();
			Case c = new Case(
					tuple.getInt(0),
					tuple.get(1),
					tuple.getInt(2),
					tuple.get(3),
					tuple.get(4),
					tuple.getDate(5),
					tuple.getTime(6)
					);
				cases.add(c);
			}
		return cases;
	}

	@Override
	public List<Case> searchByName(String string) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<CaseNote> getCaseNotesFrom(int caseId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	// TODO Duplikate entfernen?
	public List<PoI> getAllSuspects(Integer caseId) {
		Scan scan = new Scan(suspectFile, getSchema(Suspect.class));
		List<PoI> suspects = new ArrayList<PoI>();
		while (scan.moveNext()) {
			Tuple tuple = scan.current();
			PoI poi = new PoI (
					tuple.getInt(0),
					tuple.get(1),
					tuple.getDate(2)
					);
			suspects.add(poi);
		}
		
		return suspects;
	}

	@Override
	// TODO Use join
	public List<Convict> getAllConvicts(Integer caseId) {
		Scan scan = new Scan(convictedFile, getSchema(Convict.class));
		List<Convict> convicts = new ArrayList<Convict>();
		Scan scanpers = new Scan(poiFile, getSchema(PoI.class));
		while (scan.moveNext()) {
			Tuple tuple = scan.current();
			Select select = new Select(scanpers, eq(col("PoIId"), val(Integer.toString(tuple.getInt(0)))));
			if (select.moveNext()) {
				Tuple tpers = select.current();
				String poiname = tpers.get(1);
				Date birthdate = tpers.getDate(2);
				Convict c = new Convict (
						tuple.getInt(1),
						tuple.getInt(0),
						tuple.getInt(2),
						getCrimeById(tuple.getInt(2)),
						poiname,
						birthdate,
						tuple.getDate(3),
						tuple.get(4)
						);
				convicts.add(c);
			}
		}
		
		return convicts;
	}

	@Override
	public List<PoI> getAllPoIsNotLinked(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Conviction> getAllConvictions(Integer poiId) {
		Scan scan = new Scan("test", getSchema(Convict.class));
		Select select = new Select(scan, eq(col("PoIId"), val(poiId)));
		List<Conviction> convictions = new ArrayList<Conviction>();
		while (select.moveNext()) {
			Tuple tuple = select.current();
			Conviction c = new Conviction(
					tuple.getDate(3),
					poiId,
					tuple.getInt(1),
					tuple.getInt(2),
					tuple.get(4)
					);
			convictions.add(c);
		}
		return convictions;
	}
	
	

	@Override
	public void closeCase(int caseId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void openCase(int caseId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Conviction insertConviction(int caseId, int poiId,
			Date convictionDate, String sentence, int crimeId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PoINote> getPoINote(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPoiNameById(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Case> getInvolvedPoI(String poiname) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public boolean isCaseClosed(int caseId) {
		// TODO Auto-generated method stub
		return false;
	}
	

	@Override
	public Case insertCase(String name, String state, int crimeId,
			String location, Date date, Time time) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Case updateCase(int CaseId, String name, String state, int crimeId,
			String location, Date date, Time time) {
		// TODO Auto-generated method stub
		return null;
	}
	

}
