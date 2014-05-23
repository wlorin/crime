package ch.ethz.inf.dbproject.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import ch.ethz.inf.dbproject.model.meta.Entity;
import ch.ethz.inf.dbproject.model.simpleDatabase.TupleSchema;

/**
 * Object that represents a registered in user.
 */
public final class User extends Entity {

	private final int userid;
	private final String name;
	
	public User(final int userid, final String username, final String name) {
		this.userid = userid;
		this.name = name;
	}
	
	public User(ResultSet rs) throws SQLException {
		userid = rs.getInt("UserId");
		name = rs.getString("Name");
	}

	public int getUserid() {
		return userid;
	}
	
	public String getName() {
		return name;
	}
	
	@Override
	public String toString() {
		return String.format("<User: userid=%d, name='%s'>", getUserid(), getName());
	}
}
