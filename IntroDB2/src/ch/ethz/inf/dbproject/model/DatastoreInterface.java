package ch.ethz.inf.dbproject.model;

import java.util.List;

/**
 * This class should be the interface between the web application
 * and the database. Keeping all the data-access methods here
 * will be very helpful for part 2 of the project.
 */
public interface DatastoreInterface {

	public abstract Case getCaseById(final int id);

	public abstract List<Case> getAllCases();
	
	public abstract List<Case> getCaseByName(final String name);

	public abstract List<Case> getCasesByCategory(final Category category);

	public abstract List<Case> getCasesByCity(final String city);

	public abstract List<Case> getMostRecentCases();
	
	public abstract List<Case> getOldestUnsolvedCases();
			
	public abstract List<Comment> getCaseComments(final int caseId);

	public abstract List<Category> getAllCategories();
	
	public abstract User getUser(final String username, final String password);
	
	public abstract List<Case> getCaseByOwner(final int userId);
}