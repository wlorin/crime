package ch.ethz.inf.dbproject.model;

import ch.ethz.inf.dbproject.model.meta.Entity;

/**
 * Object that represents a registered in user.
 */
public final class User implements Entity {

	private final int userid;
	private final String username;
	private final String name;
	
	public User(final int userid, final String username, final String name) {
		this.userid = userid;
		this.username = username;
		this.name = name;
	}

	public int getUserid() {
		return userid;
	}
	
	public String getUsername() {
		return username;
	}

	public String getName() {
		return name;
	}	
}
