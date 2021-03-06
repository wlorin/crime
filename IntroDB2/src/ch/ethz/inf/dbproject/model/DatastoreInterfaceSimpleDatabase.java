package ch.ethz.inf.dbproject.model;

import static ch.ethz.inf.dbproject.model.simpleDatabase.conditional.Static.and;
import static ch.ethz.inf.dbproject.model.simpleDatabase.conditional.Static.col;
import static ch.ethz.inf.dbproject.model.simpleDatabase.conditional.Static.eq;
import static ch.ethz.inf.dbproject.model.simpleDatabase.conditional.Static.like;
import static ch.ethz.inf.dbproject.model.simpleDatabase.conditional.Static.val;

import java.lang.reflect.Constructor;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import ch.ethz.inf.dbproject.model.meta.Entity;
import ch.ethz.inf.dbproject.model.meta.TableName;
import ch.ethz.inf.dbproject.model.simpleDatabase.Tuple;
import ch.ethz.inf.dbproject.model.simpleDatabase.TupleSchema;
import ch.ethz.inf.dbproject.model.simpleDatabase.conditional.Condition;
import ch.ethz.inf.dbproject.model.simpleDatabase.operators.Operator;
import ch.ethz.inf.dbproject.model.simpleDatabase.operators.Scan;
import ch.ethz.inf.dbproject.model.simpleDatabase.operators.Select;
import ch.ethz.inf.dbproject.model.simpleDatabase.operators.Sort;
import ch.ethz.inf.dbproject.model.simpleDatabase.operators.StaticOperators;


public final class DatastoreInterfaceSimpleDatabase implements DatastoreInterface {

	/*
	 * Files should be received with getTableName(Class)
	 */
	
	/*
	 * Schema
	 */
	public static final String pathToDb = "c:\\database\\";
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
		return schemas.get(getTableName(clazz));
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
		final Scan scan = new Scan(getTableName(Case.class), getSchema(Case.class));	
		final Select select = new Select(scan, eq(col("Status"), val(status)));
		List<Case> cases = new ArrayList<Case>();
		while (select.moveNext()){
			final Tuple tuple = select.current();					
			Case c = new Case(tuple);
			cases.add(c);
		}
		return cases;
	}

	@Override
	public List<Case> getMostRecentCases(int number) {
		final Scan scan = new Scan(getTableName(Case.class), getSchema(Case.class));	
		final Select select = new Select(scan, eq(col("Status"), val("open"))); 
		final Sort sort = new Sort(select, "Date", false);
		List<Case> cases = new ArrayList<Case>();
		for (int i = 0; i < number; i++) {
			if (sort.moveNext()) {			
				final Tuple tuple = sort.current();
				Case c = new Case(tuple);
				cases.add(c);
			}
		}
		return cases;
	}

	@Override
	public List<Case> getOldestUnsolvedCases(int number) {		
		final Scan scan = new Scan(getTableName(Case.class), getSchema(Case.class));	
		final Select select = new Select(scan, eq(col("Status"), val("open"))); 
		final Sort sort = new Sort(select, "Date", true);
		List<Case> cases = new ArrayList<Case>();
		for (int i = 0; i < number; i++) {
			if (sort.moveNext()) {			
				final Tuple tuple = sort.current();
				Case c = new Case(tuple);
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
		
		final Scan scan = new Scan(getTableName(User.class), getSchema(User.class));
		final Select select = new Select(scan, and(eq(col("Name"), val(name)), eq(col("Password"), val(password))));
		User user = null;
		if (select.moveNext()) {
			Tuple tuple = select.current();
			user = new User(tuple);
		}
		return user;
	}

	@Override
	public void removeSuspect(int caseId, int poiId) {
		StaticOperators.delete(getTableName(Suspect.class), getSchema(Suspect.class), and(eq(col("CaseId"), val(caseId)), eq(col("PoIId"), val(poiId))));		
	}

	@Override
	public void deleteCase(int caseId) {
		StaticOperators.delete(getTableName(Case.class), getSchema(Case.class), eq(col("CaseId"), val(caseId)));
		
	}

	@Override
	public void deletePoI(int poiId) {
		StaticOperators.delete(getTableName(PoI.class), getSchema(PoI.class), eq(col("PoIId"), val(poiId)));
		
	}

	@Override
	public void deletePoINote(int poinoteId) {
		StaticOperators.delete(getTableName(PoINote.class), getSchema(PoINote.class), eq(col("PoINoteId"), val(poinoteId)));
		
	}
	
	@Override
	public void deleteCaseNote(int casenoteId) {
		StaticOperators.delete(getTableName(CaseNote.class), getSchema(CaseNote.class), eq(col("CaseNoteId"), val(casenoteId)));
		
	}
	
	@Override
	public User insertUser(String name, String password) {
		int userid = StaticOperators.insert(getTableName(User.class), getSchema(User.class), new String[] {null, name, password});
		if (userid > 0) {
			return getById(userid, User.class);
		}
		return null;
	}

	@Override
	public Suspect insertSuspect(int caseId, int poiId) {
		StaticOperators.insert(getTableName(Suspect.class), getSchema(Suspect.class), 
				new String[] {Integer.toString(poiId), Integer.toString(caseId)});

		return new Suspect(caseId, poiId);
	}
	
	private String getCurrentTimestamp() {
		Date now = Calendar.getInstance().getTime();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = format.format(now);
		return time;
	}

	@Override
	public CaseNote insertCaseNote(String comment, int caseId, int userid) {
		int noteid = StaticOperators.insert(getTableName(CaseNote.class), getSchema(CaseNote.class), 
				new String[] {null, Integer.toString(caseId), getCurrentTimestamp(), comment, Integer.toString(userid)});		
		return getById(noteid, CaseNote.class);
	}

	@Override
	public Crime insertCrime(String crimeName) {
		int crimeid = StaticOperators.insert(getTableName(Crime.class), getSchema(Crime.class), new String[] {null, crimeName});
		return getById(crimeid, Crime.class);
	}

	@Override
	public PoI insertPoI(String name, Date birthdate) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String birth = (birthdate == null) ? null : format.format(birthdate);
		int poiId = StaticOperators.insert(getTableName(PoI.class), getSchema(PoI.class), new String[] {null, name, birth , null});
		return getById(poiId, PoI.class);
	}

	@Override
	public PoI updatePoI(Long id, String name, Date birthdate) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String sdate = (birthdate == null) ? null : format.format(birthdate);
		StaticOperators.update(getTableName(PoI.class), getSchema(PoI.class), 
				new String[] {"PoIId", "Name", "Birthdate"}, 
				new String[] {Long.toString(id), name, sdate},
				 eq(col("PoIId"), val(Long.toString(id))));
		return getById(id, PoI.class);
	}

	@Override
	public PoINote insertPoINote(String comment, int poiId, int userid) {
		int noteid = StaticOperators.insert(getTableName(PoINote.class), getSchema(PoINote.class), 
				new String[] {null, Integer.toString(poiId), getCurrentTimestamp(), comment, Integer.toString(userid)});		
		return getById(noteid, PoINote.class);
	}

	@Override
	public String getCrimeById(int id) {
		Scan scan = new Scan(getTableName(Crime.class), getSchema(Crime.class));
		Select select = new Select(scan, eq(col("CrimeId"), val(id)));
		if (select.moveNext()) {
			Tuple tuple = select.current();
			return tuple.getString("Crime");
		}
		return null;
	}
	
	public int getCaseIdByName(String name) {
		Scan scan = new Scan(getTableName(Case.class), getSchema(Case.class));
		Select select = new Select(scan, eq(col("Name"), val(name)));
		if (select.moveNext()) {
			return select.current().getInt("CaseId");
		}
		return -1;
	}

	@Override
	public String getCasenameById(int id) {
		Scan scan = new Scan(getTableName(Case.class), getSchema(Case.class));
		Select select = new Select(scan, eq(col("CaseId"), val(id)));
		if (select.moveNext()) {
			Tuple tuple = select.current();
			return tuple.getString("Name");
		}
		return null;
	}

	public int getCrimeIdByName(String name) {
		Scan scan = new Scan(getTableName(Crime.class), getSchema(Crime.class));
		Select select = new Select(scan, eq(col("Crime"), val(name)));
		if (select.moveNext()) {
			return select.current().getInt(0);
		}
		return -1;
	}
	
	@Override
	@Deprecated
	public List<Case> getCasesByCrime(String category) {
		int id = getCrimeIdByName(category);
		return getCasesByCrime(id);
	}
	public List<Case> getCasesByCrime(int crimeId) {
		final Scan scan = new Scan(getTableName(Case.class), getSchema(Case.class));	
		final Select select = new Select(scan, eq(col("CrimeId"), val(crimeId))); 
		List<Case> cases = new ArrayList<Case>();
		while (select.moveNext()) {			
			final Tuple tuple = select.current();
			Case c = new Case(tuple);
			cases.add(c);
		}
		return cases;
	}

	@Override
	public List<Case> getProjectsWithoutCategory() {
		final Scan scan = new Scan(getTableName(Case.class), getSchema(Case.class));	
		final Select select = new Select(scan, eq(col("CrimeId"), val(null))); 
		List<Case> cases = new ArrayList<Case>();
		while (select.moveNext()) {			
			final Tuple tuple = select.current();
			Case c = new Case(tuple);
				cases.add(c);
			}
		return cases;
	}

	@Override
	public List<Case> searchByName(String string) {
		List<Case> cases = new ArrayList<Case>();
		Scan scan = new Scan(getTableName(Case.class), getSchema(Case.class));
		Select select = new Select(scan, like(col("Name"), val("%" + string + "%")));
		while (select.moveNext()) {
			cases.add(new Case(select.current()));
		}
		return cases;
	}
	
	public List<PoI> searchPoIByName(String name) {
		List<PoI> pois = new ArrayList<PoI>();
		Scan scan = new Scan(getTableName(PoI.class), getSchema(PoI.class));
		Select select = new Select(scan, like(col("Name"), val("%" + name + "%")));
		while (select.moveNext()) {
			pois.add(new PoI(select.current()));
		}
		return pois;
	}
	
	
	@Override
	public List<CaseNote> getCaseNotesFrom(int caseId) {
		List<CaseNote> notes = new ArrayList<CaseNote>();
		Scan scan = new Scan(getTableName(CaseNote.class), getSchema(CaseNote.class));
		Select select = new Select(scan, eq(col("CaseId"), val(caseId)));
		while (select.moveNext()) {
			Tuple tuple = select.current();
			CaseNote note = null;
			try {
				note = new CaseNote(tuple);
			} catch (Exception e) {
				e.printStackTrace();
			}
			notes.add(note);
		}
		return notes;
	}

	@Override
	public List<PoI> getAllSuspects(Integer caseId) {
		Operator suspect = new Scan(getTableName(Suspect.class), getSchema(Suspect.class)).as("s");
		Operator suspectSelect = new Select(suspect, eq(col("s.CaseId"), val(caseId)));
		Scan poi = new Scan(getTableName(PoI.class), getSchema(PoI.class));
		Operator join = poi.join(suspectSelect, eq(col("s.PoIId"), col("PoIId")));
		Operator reducedSchema = new ch.ethz.inf.dbproject.model.simpleDatabase.operators.Case(join, getSchema(PoI.class));
		List<PoI> suspects = new ArrayList<PoI>();
		while (reducedSchema.moveNext()) {
			Tuple tuple = reducedSchema.current();
			suspects.add(new PoI (tuple));
		}
		return suspects;
	}

	@Override
	// TODO Use join
	public List<Convict> getAllConvicts(Integer caseId) {
		/*String sql = "SELECT poi.*, cr.CrimeId, cr.Crime, c.CaseId, c.Date as ConvictionDate, c.Sentence  
			FROM PoI poi " +
			"INNER JOIN Convicted c ON (poi.PoIId = c.PoIId AND c.CaseId=" + caseId + ") " +
			"INNER JOIN Crime cr ON (cr.CrimeId=c.CrimeId)";
		*/
		
		List<Convict> convicts = new ArrayList<Convict>();
		
		Operator poi = new Scan(getTableName(PoI.class), getSchema(PoI.class)).as("poi");
		Operator conviction = new Scan(getTableName(Conviction.class), getSchema(Conviction.class)).as("c");
		Operator crime = new Scan(getTableName(Crime.class), getSchema(Crime.class)).as("cr");
		Operator poiConviction = poi.join(conviction, and(eq(col("poi.PoIId"), col("c.PoIId")), eq(col("c.CaseId"), val(caseId))));
		Operator totalJoin = poiConviction.join(crime, eq(col("cr.CrimeId"), col("c.CrimeId")));
		
		while (totalJoin.moveNext()) {
			Tuple t = totalJoin.current();
			Convict c = new Convict(t.getInt("c.CaseId"), 
					t.getInt("poi.PoIId"), 
					t.getInt("cr.CrimeId"), 
					t.getString("cr.Crime"), 
					t.getString("poi.Name"), 
					t.getDate("poi.birthDate"), 
					t.getDate("c.Date"), 
					t.getString("c.Sentence"));
			convicts.add(c);
		}
		
		return convicts;
	}


	@Override
	/*
	String sql = "SELECT poi.* FROM " +
			tableName +  " poi LEFT JOIN Suspect s ON (poi.PoIId = s.PoIId AND s.CaseId=" + id + ") " +
			"WHERE IsNull(s.CaseId);";
	 */
	public List<PoI> getAllPoIsNotLinkedToCase(Integer caseId) {
		List<PoI> pois = new ArrayList<PoI>();
		Operator suspect = new Scan(getTableName(Suspect.class), getSchema(Suspect.class)).as("s");
		Operator poi = new Scan(getTableName(PoI.class), getSchema(PoI.class)).as("poi");
		Condition cond = and(eq(col("poi.PoIId"), col("s.PoIId")), eq(col("s.CaseId"), val(caseId)));
		Operator poiSuspect = poi.joinLeft(suspect, cond);
		Select isNull = new Select(poiSuspect, eq(col("s.CaseId"), val(null)));
		
		while (isNull.moveNext()) {
			Tuple tuple = poiSuspect.current();
			pois.add(getById(tuple.getInt("poi.PoIId"), PoI.class));
		}
			
		return pois;
	}

	@Override
	public List<Conviction> getAllConvictions(Integer poiId) {
		Scan scan = new Scan(getTableName(Conviction.class), getSchema(Conviction.class));
		Select select = new Select(scan, eq(col("PoIId"), val(poiId)));
		List<Conviction> convictions = new ArrayList<Conviction>();
		while (select.moveNext()) {
			convictions.add(new Conviction(select.current()));
		}
		return convictions;
	}

	@Override
	public void closeCase(int caseId) {
		Case c = getById(caseId, Case.class);
		updateCase(c.getId(), c.getName(), "closed", c.getCrimeId(), c.getLocation(), c.getDate(), (Time) c.getTime() );
		
	}

	@Override
	public void openCase(int caseId) {
		Case c = getById(caseId, Case.class);
		updateCase(c.getId(), c.getName(), "open", c.getCrimeId(), c.getLocation(), c.getDate(), (Time) c.getTime() );
		
	}

	@Override
	public Conviction insertConviction(int caseId, int poiId,
			Date convictionDate, String sentence, int crimeId) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String sdate = (convictionDate == null) ? null : format.format(convictionDate);
		StaticOperators.insert(getTableName(Conviction.class), 
				getSchema(Conviction.class), new String[] {Integer.toString(poiId), Integer.toString(caseId),
			Integer.toString(crimeId), sdate, sentence });
		
		return new Conviction(convictionDate, poiId, caseId, crimeId, sentence);
	}

	@Override
	public List<PoINote> getPoINote(int id) {
		Scan scan = new Scan(getTableName(PoINote.class), getSchema(PoINote.class));
		Select select = new Select(scan, eq(col("PoIId"), val(id)));
		List<PoINote> notes = new ArrayList<PoINote>();
		while (select.moveNext()) {
			PoINote note = null;
			try {
				note = new PoINote(select.current());
			} catch (Exception e) {
				e.printStackTrace();
			}
			notes.add(note);
		}
		return notes;
	}

	@Override
	public String getPoiNameById(Integer id) {
		Scan scan = new Scan(getTableName(PoI.class), getSchema(PoI.class));
		Select select = new Select(scan, eq(col("PoIId"), val(id)));
		if (select.moveNext()) {
			return select.current().getString(1);
		}
		return null;
	}

	@Override
	public List<Case> getInvolvedPoI(String poiname) {
		List<Case> cases = new ArrayList<Case>();
		List<PoI> pois = searchPoIByName(poiname);
		Scan susp = new Scan(getTableName(Suspect.class), getSchema(Suspect.class));
		Scan conv = new Scan(getTableName(Conviction.class), getSchema(Conviction.class));
		Condition condition = null;
		for (PoI poi : pois) {
			Condition newCondition = eq(col("PoIId"), val(poi.getId()));
			if (condition != null) {
				condition = and(condition, newCondition);
			}
			else {
				condition = newCondition;
			}
		}
		Select s1 = new Select(susp, condition);
		while (s1.moveNext()) {
			Tuple t = s1.current();
			cases.add(getById(t.getInt(1), Case.class));
		}
		Select s2 = new Select(conv, condition);
		while (s2.moveNext()) {
			Tuple t = s2.current();
			cases.add(getById(t.getInt(1), Case.class));
		}
		return cases;
	}


	@Override
	public boolean isCaseClosed(int caseId) {
		Scan scan = new Scan(getTableName(Case.class), getSchema(Case.class));
		Select select = new Select(scan, eq(col("CaseId"), val(caseId)));
		if (select.moveNext()) {
		 if (select.current().getString(3).equals("false"))
			return true;	
		}
		return false;
	}
	

	@Override
	public Case insertCase(String name, String state, int crimeId,
			String location, Date date, Time time) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String sdate = (date == null) ? null : format.format(date);
		String stime = (time == null) ? null : new SimpleDateFormat("HH:mm:ss").format(time);
		int caseid = StaticOperators.insert(getTableName(Case.class), getSchema(Case.class), new String[] {
			null, name, Integer.toString(crimeId), state, sdate, stime, location
			});
		return getById(caseid, Case.class);
	}
	
	@Override
	public Case updateCase(int CaseId, String name, String state, int crimeId,
			String location, Date date, Time time) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String sdate = (date == null) ? null : format.format(date);
		String stime = (time == null) ? null : new SimpleDateFormat("HH:mm:ss").format(time);
		StaticOperators.update(getTableName(Case.class), getSchema(Case.class), 
				new String[] {"CaseId", "Name", "CrimeId", "Status", "Date", "Time", "Location"}, 
				new String[] {String.valueOf(CaseId), name, String.valueOf(crimeId), state, sdate, stime, location},
				 eq(col("CaseId"), val(CaseId)));
		return getById(CaseId, Case.class);
	}
	public void deleteConviction(Integer caseId, int poiId, int crimeId) {
		// runQuery("DELETE FROM Convicted WHERE CaseId=" + caseId + " AND PoIId=" + poiId + " AND CrimeId=" + crimeid);
		StaticOperators.delete(getTableName(Conviction.class), getSchema(Conviction.class), 
				and(
					and(
						eq(col("CaseId"), val(caseId)), 
						eq(col("PoIId"), val(poiId))
					), 
					eq(col("CrimeId"), val(crimeId))
				)
		);	
	}
		
	@Override
	public int getCaseIdFromCaseNote(int casenoteid) {
		CaseNote note = getById(casenoteid, CaseNote.class);
		return note.getCaseId();
	}

	@Override
	public int getPoIIdFromPoINote(int noteId) {
		PoINote note = getById(noteId, PoINote.class);
		return note.getPoiId();
	}
	

}
