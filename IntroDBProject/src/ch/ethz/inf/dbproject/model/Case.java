package ch.ethz.inf.dbproject.model;

import java.sql.ResultSet;

import java.sql.SQLException;

import ch.ethz.inf.dbproject.model.meta.Entity;

import java.util.Date;

public final class Case implements Entity {
	
	/**
	 * TODO The properties of the case should be added here
	 */
	private final int caseId;
	private final String description;
	private final String status;
	private final String location;
	private final Date date;
	private final Date time;
	
	/**
	 * Construct a new case.
	 * 
	 * @param description		The name of the case
	 */
	public Case(	final int id, final String description, final String status, final String location, final Date date, final Date time) {
		this.caseId = id;
		this.description = description;
		this.status = status;
		this.location = location;
		this.date = date;
		this.time = time;
	}
	
	public Case(	final ResultSet rs) throws SQLException {
		// TODO These need to be adapted to your schema
		// TODO Extra properties need to be added
		this.caseId = rs.getInt("caseId");
		this.description = rs.getString("description");
		this.status = rs.getString("status");
		this.location = rs.getString("location");
		this.date = rs.getDate("date");
		this.time = rs.getTime("time");
	}

	/**
	 * HINT: In eclipse, use Alt + Shirt + S menu and choose:
	 * "Generate Getters and Setters to auto-magically generate
	 * the getters. 
	 */
	public String getDescription() {
		return description;
	}
	
	public String getStatus() {
		return status;
	}

	public int getCaseId() {
		return caseId;
	}

	public String getLocation() {
		return location;
	}
	
	public Date getDate() {
		return date;
	}
	
	public Date getTime() {
		return time;
	}
}