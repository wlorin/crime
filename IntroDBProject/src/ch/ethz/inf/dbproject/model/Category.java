package ch.ethz.inf.dbproject.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import ch.ethz.inf.dbproject.model.meta.Entity;

/**
 * Object that represents a category of project (i.e. Theft, Assault...) 
 */
public final class Category implements Entity {

	/**
	 * TODO All properties need to be added here 
	 */	
	private final int id;
	private final String crime;

	public Category(final int id, final String name) {
		this.crime = name;
		this.id = id;
	}
	
	public Category(ResultSet rs) throws SQLException {
		id = rs.getInt("CrimeId");
		crime = rs.getString("Crime");
	}

	public final String getName() {
		return crime;
	}
	
}
