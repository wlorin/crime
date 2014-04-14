package ch.ethz.inf.dbproject.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import ch.ethz.inf.dbproject.model.meta.Entity;

/**
 * Object that represents a user comment.
 */
public abstract class Note implements Entity {

	private final String username;
	private final String note;
	
	public Note(ResultSet rs) throws SQLException {
		username = new DatastoreInterface().getById(rs.getInt("UserId"), User.class).getName();
		note = rs.getString("Note");
	}

	public String getUsername() {
		return username;
	}

	public String getNote() {
		return note;
	}
}
