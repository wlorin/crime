package ch.ethz.inf.dbproject.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import ch.ethz.inf.dbproject.model.meta.Entity;

/**
 * Object that represents a registered in user.
 */
public final class User implements Entity {

	private final int userid;
	private final String password;
	private final String name;
	
	public User(final int userid, final String password, final String name) {
		this.userid = userid;
		this.password = password;
		this.name = name;
	}
	
	public User(ResultSet rs) throws SQLException {
		throw new UnsupportedOperationException("Constructor not yet implemented");
	}

	public int getUserid() {
		return userid;
	}
	
	public String getPassword() {
		return password;
	}

	public String getName() {
		return name;
	}	
}
