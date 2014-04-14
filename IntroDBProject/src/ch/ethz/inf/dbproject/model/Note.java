package ch.ethz.inf.dbproject.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import ch.ethz.inf.dbproject.model.meta.Entity;

/**
 * Object that represents a user comment.
 */
public abstract class Note implements Entity {

	private final String username;
	private final String note;
	private final Timestamp timestamp;
	
	public Note(ResultSet rs) throws SQLException {
		username = new DatastoreInterface().getById(rs.getLong("UserId"), User.class).getName();
		note = rs.getString("Note");
		timestamp = rs.getTimestamp("Timestamp");
	}

	public String getUsername() {
		return username;
	}

	public String getNote() {
		return note;
	}

	public Timestamp getTimestamp() {
		return timestamp;
	}
}
