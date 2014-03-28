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
	private final String name;

	public Category(final String name) {
		super();
		this.name = name;
	}
	
	public Category(ResultSet rs) throws SQLException {
		throw new UnsupportedOperationException("Constructor not yet implemented");
	}

	public final String getName() {
		return name;
	}
	
}
