package ch.ethz.inf.dbproject.model;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

/**
 * This class should be the interface between the web application
 * and the database. Keeping all the data-access methods here
 * will be very helpful for part 2 of the project.
 */
public final class DatastoreInterfaceMySQL implements DatastoreInterface{

	//FIXME This is a temporary list of projects that will be displayed until all the methods have been properly implemented
	private final static Case[] staticCases = new Case[] { 
			new Case(0, "Stolen car", "open", 10000), 
			new Case(1, "High speed", "closed", 250000),
			new Case(2, "Financial fraud", "open", 1000000),
			new Case(3, "Pollution", "closed", 1000000000),
		};
	private final static List<Case> staticCaseList = new ArrayList<Case>();
	static {
		for (int i = 0; i < staticCases.length; i++) {
			staticCaseList.add(staticCases[i]);
		}
	}
	
	@SuppressWarnings("unused")
	private Connection sqlConnection;

	public DatastoreInterfaceMySQL() {
		this.sqlConnection = null;
	}
	
	@Override
	public final Case getCaseById(final int id) {
	
		/**
		 * TODO this method should return the case with the given id
		 */
		
		if (id < staticCases.length) {
			return staticCases[id];
		} else {
			return null;
		}
		
	}
	
	@Override
	public final List<Case> getAllCases() {

		/**
		 * TODO this method should return all the cases in the database
		 */
			
		/*
		//Code example for DB access
		try {
			
			final Statement stmt = this.sqlConnection.createStatement();
			final ResultSet rs = stmt.executeQuery("Select ...");
		
			final List<Project> cases = new ArrayList<Project>(); 
			while (rs.next()) {
				cases.add(new Case(rs));
			}
			
			rs.close();
			stmt.close();

			return cases;
			
		} catch (final SQLException ex) {			
			ex.printStackTrace();
			return null;			
		}
		
		*/
		
		// If you chose to use PreparedStatements instead of statements, you should prepare them in the constructor of DatastoreInterface.
		
		// For the time being, we return some bogus cases
		return staticCaseList;
	}
	

	//TODO Implement all missing data access methods

	@Override
	public List<Case> getCaseByName(String name) {
		// TODO 
		return null;
	}

	@Override
	public List<Case> getCasesByCategory(Category category) {
		// TODO 
		return null;
	}

	@Override
	public List<Case> getCasesByCity(String city) {
		// TODO 
		return null;
	}

	@Override
	public List<Case> getMostRecentCases() {
		// TODO 
		return null;
	}

	@Override
	public List<Case> getOldestUnsolvedCases() {
		// TODO 
		return null;
	}

	@Override
	public List<Comment> getCaseComments(int projectId) {
		// TODO 
		return null;
	}

	@Override
	public List<Category> getAllCategories() {
		// TODO 
		return null;
	}

	@Override
	public User getUser(String username, String password) {
		// TODO
		return null;
	}

	@Override
	public List<Case> getCaseByOwner(int userId) {
		// TODO
		return null;
	}	
}
