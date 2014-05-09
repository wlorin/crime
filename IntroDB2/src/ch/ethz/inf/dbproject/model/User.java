package ch.ethz.inf.dbproject.model;

/**
 * Object that represents a registered in user.
 */
public final class User {

	private final String username;
	private final String name;
	
	public User(
		final String username,
		final String name
	) {
		this.username = username;
		this.name = name;
	}

	public String getUsername() {
		return username;
	}

	public String getName() {
		return name;
	}
	
}
