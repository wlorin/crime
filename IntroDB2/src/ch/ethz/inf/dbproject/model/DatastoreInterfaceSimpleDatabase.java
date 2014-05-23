package ch.ethz.inf.dbproject.model;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
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
import static ch.ethz.inf.dbproject.model.simpleDatabase.conditional.Static.*;


public final class DatastoreInterfaceSimpleDatabase implements DatastoreInterface {

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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Case> getMostRecentCases(int number) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Case> getOldestUnsolvedCases(int number) {		
		final Scan scan = new Scan(getTableName(Case.class), getSchema(Case.class));
		final Sort sort = new Sort(scan, "Date", false);
		List<Case> cases = new ArrayList<Case>();
		
		for (int i = 0; i < number; i++ ){
			if (sort.moveNext()) {	
				final Tuple tuple = sort.current();
				Case c = new Case(
						tuple.getInt(0),
						tuple.getString(1),
						tuple.getInt(2),
						tuple.getString(3),
						tuple.getString(4),
						tuple.getDate(5),
						tuple.getTime(6)
						);
			}
		}
		return null;
	}

	@Override
	public User tryGetUserFromCredentials(String name, String password) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removeSuspect(int caseId, int poiId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public User insertUser(String name, String password) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Suspect insertSuspect(int caseId, int poiId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CaseNote insertComment(String comment, int caseId, int userid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Crime insertCrime(String crimeName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PoI insertPoI(String name, Date birthdate) {
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCasenameById(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Case> getProjectsByCategory(String category) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Case> getProjectsWithoutCategory() {
		// TODO Auto-generated method stub
		return null;
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
	public List<PoI> getAllSuspects(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Convict> getAllConvicts(Integer caseId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PoI> getAllPoIsNotLinked(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Conviction> getAllConvictions(Integer id) {
		// TODO Auto-generated method stub
		return null;
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
	public void deleteCase(int caseId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deletePoI(int poiId) {
		// TODO Auto-generated method stub
		
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
