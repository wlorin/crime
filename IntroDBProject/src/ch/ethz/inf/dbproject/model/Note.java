package ch.ethz.inf.dbproject.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import ch.ethz.inf.dbproject.model.meta.Entity;

/**
 * Object that represents a user comment.
 */
public class Note implements Entity {

	private final String username;
	private final String comment;
	
	public Note(final String username, final String comment) {
		this.username = username;
		this.comment = comment;
	}
	
	public Note(ResultSet rs) throws SQLException {
		throw new UnsupportedOperationException("Constructor not yet implemented");
	}

	public String getUsername() {
		return username;
	}

	public String getComment() {
		return comment;
	}
}