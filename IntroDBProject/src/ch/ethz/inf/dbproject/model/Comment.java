package ch.ethz.inf.dbproject.model;

import ch.ethz.inf.dbproject.model.meta.Entity;

/**
 * Object that represents a user comment.
 */
public class Comment implements Entity {

	private final String username;
	private final String comment;
	
	public Comment(final String username, final String comment) {
		this.username = username;
		this.comment = comment;
	}

	public String getUsername() {
		return username;
	}

	public String getComment() {
		return comment;
	}	
}
