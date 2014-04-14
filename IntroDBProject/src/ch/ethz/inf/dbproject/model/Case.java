package ch.ethz.inf.dbproject.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.Date;
import java.util.List;

import ch.ethz.inf.dbproject.model.meta.Entity;

public final class Case implements Entity {
	
	/**
	 * TODO The properties of the case should be added here
	 */
	private final int id;
	final private String name;
	private final int crimeId;
	private final String status;
	private final String location;
	private final Date date;
	private final Time time;
	
	/**
	 * Construct a new case.
	 * 
	 * @param description		The name of the case
	 */
	public Case(	final int id, final String name, final int crimeId, final String status, final String location, final Date date, final Time time) {
		this.id = id;
		this.name = name;
		this.crimeId = crimeId;
		this.status = status;
		this.location = location;
		this.date = date;
		this.time = time;
	}
	
	public Case(	final ResultSet rs) throws SQLException {
		// TODO These need to be adapted to your schema
		// TODO Extra properties need to be added
		this.id = rs.getInt("caseId");
		this.name = rs.getString("Name");
		this.crimeId = rs.getInt("crimeId");
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
	public int getCrimeId() {
		return crimeId;
	}

	public String getName() {
		return name;
	}

	public String getCrime() {
		final DatastoreInterface dbInterface = new DatastoreInterface();
		return dbInterface.getCrimeById(crimeId);
	}
	
	public String getStatus() {
		return status;
	}

	public int getId() {
		return id;
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

	public List<CaseNote> getCaseNotes() {
		return new DatastoreInterface().getCaseNotesFrom(this.getId());
	}

	public boolean isOpen() {
		return "open".equals(getStatus());
	}
}