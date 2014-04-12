package ch.ethz.inf.dbproject.model;

import java.sql.ResultSet;

import java.sql.SQLException;

import java.util.Date;

import ch.ethz.inf.dbproject.model.meta.Entity;

public final class PoI implements Entity {

	private final int id;
	private final String name;
	private final Date birthdate;


	public PoI(	final int id, final String name, final Date birthdate) {
		this.id = id;
		this.name = name;
		this.birthdate = birthdate;
	}
	
	public PoI(	final ResultSet rs) throws SQLException {
		this.id = rs.getInt("PoIId");
		this.name = rs.getString("name");
		this.birthdate = rs.getDate("birthdate");
	}

	/**
	 * HINT: In eclipse, use Alt + Shirt + S menu and choose:
	 * "Generate Getters and Setters to auto-magically generate
	 * the getters. 
	 */
	public String getName() {
		return name;
	}
	
	public Date getBirthdate() {
		return birthdate;
	}

	public int getId() {
		return id;
	}
}