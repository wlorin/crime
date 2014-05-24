package ch.ethz.inf.dbproject.model;

import java.sql.Time;
import java.util.Date;
import java.util.List;

import ch.ethz.inf.dbproject.model.meta.Entity;

/**
 * This class should be the interface between the web application
 * and the database. Keeping all the data-access methods here
 * will be very helpful for part 2 of the project.
 */
public interface DatastoreInterface {

	public abstract <T extends Entity> T getById(final Long id, Class<T> clazz);

	public abstract <T extends Entity> List<T> getAll(Class<T> clazz);
	
	public abstract List<Case> getByStatus(String status);

	public abstract List<Case> getMostRecentCases(int number);
	
	public abstract List<Case> getOldestUnsolvedCases(int number);
			
	public abstract User tryGetUserFromCredentials(String name, String password);

	public abstract void removeSuspect(int caseId, int poiId);
	
	public abstract User insertUser(String name, String password);
	
	public abstract Suspect insertSuspect(int caseId, int poiId);
	
	public abstract Case insertCase(String name, String state, int crimeId, String location, Date date, Time time);
	
	public abstract Case updateCase(int CaseId, String name, String state, int crimeId, String location, Date date, Time time);
	
	public abstract CaseNote insertCaseNote(String comment, int caseId, int userid);
	
	public abstract Crime insertCrime(String crimeName);
	
	public abstract PoI insertPoI(String name, Date birthdate);
	
	public abstract PoI updatePoI(Long id, String name, Date birthdate);
	
	public abstract PoINote insertPoINote(String comment, int poiId, int userid);
	
	public abstract String getCrimeById(int id);
	
	public abstract String getCasenameById(int id);
	
	public abstract List<Case> getCasesByCrime(String category);
	
	public abstract List<Case> getProjectsWithoutCategory();
	
	public abstract List<Case> searchByName(String string);
	
	public abstract List<CaseNote> getCaseNotesFrom(int caseId);
	
	public abstract List<PoI> getAllSuspects(Integer caseId);
	
	public abstract List<Convict> getAllConvicts(Integer caseId);
	
	public abstract List<PoI> getAllPoIsNotLinkedToCase(Integer id);
	
	public abstract List<Conviction> getAllConvictions(Integer poiId);
	
	public abstract void closeCase(int caseId);
	
	public abstract void openCase(int caseId);
	
	public abstract Conviction insertConviction(int caseId, int poiId, Date convictionDate, String sentence, int crimeId);
	
	public abstract List<PoINote> getPoINote(int id);
	
	public abstract String getPoiNameById(Integer id);
	
	public abstract List<Case> getInvolvedPoI(String poiname);
	
	public abstract void deleteCase(int caseId);
	
	public abstract void deletePoI(int poiId);
	
	public abstract void deleteCaseNote(int casenoteId);
	
	public abstract void deletePoINote(int poinoteId);
	
	public abstract boolean isCaseClosed(int caseId);
	
}