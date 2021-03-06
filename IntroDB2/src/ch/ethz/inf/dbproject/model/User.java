package ch.ethz.inf.dbproject.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import ch.ethz.inf.dbproject.model.meta.Entity;
import ch.ethz.inf.dbproject.model.simpleDatabase.Tuple;

/**
 * Object that represents a registered in user.
 */
public final class User extends Entity {

	private final int userid;
	private final String name;
	private final String password;
	
	public User(final int userid, final String username, final String name, final String password) {
		this.userid = userid;
		this.name = name;
		this.password = password;
	}
	
	public User(ResultSet rs) throws SQLException {
		userid = rs.getInt("UserId");
		name = rs.getString("Name");
		password = rs.getString("Password");
	}
	public User(Tuple rs) {
		userid = rs.getInt("UserId");
		name = rs.getString("Name");
		password = rs.getString("Password");
	}

	public int getUserid() {
		return userid;
	}
	
	public String getName() {
		return name;
	}
	public String getPassword() {
		return password;
	}
	
	@Override
	public String toString() {
		return String.format("<User: userid=%d, name='%s', password='%s'>", getUserid(), getName(), getPassword());
	}
}
