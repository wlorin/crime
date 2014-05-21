package ch.ethz.inf.dbproject.model;

import static ch.ethz.inf.dbproject.model.simpleDatabase.conditional.Static.*;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ch.ethz.inf.dbproject.model.meta.Entity;
import ch.ethz.inf.dbproject.model.Case;
import ch.ethz.inf.dbproject.model.simpleDatabase.Tuple;
import ch.ethz.inf.dbproject.model.simpleDatabase.TupleSchema;
import ch.ethz.inf.dbproject.model.simpleDatabase.operators.Scan;
import ch.ethz.inf.dbproject.model.simpleDatabase.operators.Select;
import ch.ethz.inf.dbproject.model.simpleDatabase.operators.Sort;


public final class DatastoreInterfaceSimpleDatabase implements DatastoreInterface {

	public DatastoreInterfaceSimpleDatabase() {
	}
	
	/*
	 * Schema
	 */
	
	private TupleSchema schemaCase = TupleSchema.build().
			intCol("CaseId").asPrimary().asAutoIncrement().
			varcharCol("Name", 100).
			intCol("CrimeId").
			varcharCol("Status", 20).
			dateCol("Date").
			timeCol("Time").
			varcharCol("Location", 50).
			build();
	
	/* (non-Javadoc)
	 * @see ch.ethz.inf.dbproject.model.DatastoreInterface#getCaseById(int)
	 */

	public final Case getCaseById(final int id) {
	
		/**
		 * TODO this method should return the case with the given id
		 */
		final Scan scan = new Scan("bla", TupleSchema.build().intCol("blubb").build());
		
		final Select select = new Select(scan, eq(col("id"), val(id)));
		
		if (select.moveNext()) {
			
			final Tuple tuple = select.current();
			
			return null;
			
		} else {
			
			return null;
			
		}		
	}


	public final List<Category> getAllCategories() {

		/**
		 * TODO This method should return all the different categories in the
		 * database.
		 * 
		 * For the time being we return some random values.
		 */
		final List<Category> categories = new ArrayList<Category>();
		categories.add(new Category("Theft"));
		categories.add(new Category("Fraud"));
		return categories;
	}


	@Override
	public <T extends Entity> T getById(Long id, Class<T> clazz) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T extends Entity> List<T> getAll(Class<T> clazz) {
		// TODO Auto-generated method stub
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
		final Scan scan = new Scan("bla", schemaCase);
		final Sort sort = new Sort(scan, "Date", false);
		List<Case> cases = new ArrayList<Case>();
		
		for (int i = 0; i < number; i++ ){
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
