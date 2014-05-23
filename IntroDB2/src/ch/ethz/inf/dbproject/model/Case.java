package ch.ethz.inf.dbproject.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.Date;
import java.util.List;

import ch.ethz.inf.dbproject.model.meta.Entity;
import ch.ethz.inf.dbproject.model.simpleDatabase.Tuple;
import ch.ethz.inf.dbproject.model.simpleDatabase.TupleSchema;

public final class Case extends Entity {
	
	//schema
	
	
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
		this.id = rs.getInt("caseId");
		this.name = rs.getString("Name");
		this.crimeId = rs.getInt("crimeId");
		this.status = rs.getString("status");
		this.location = rs.getString("location");
		this.date = rs.getDate("date");
		this.time = rs.getTime("time");
	}
	
	public Case(final Tuple tuple) throws Exception {
		this.id = tuple.getInt("caseId");
		this.name = tuple.getString("Name");
		this.crimeId = tuple.getInt("crimeId");
		this.status = tuple.getString("status");
		this.location = tuple.getString("location");
		this.date = tuple.getDate("date");
		this.time = tuple.getTime("time");
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
		final DatastoreInterfaceMySQL dbInterface = new DatastoreInterfaceMySQL();
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
		return new DatastoreInterfaceMySQL().getCaseNotesFrom(this.getId());
	}

	public boolean isOpen() {
		return "open".equals(getStatus());
	}
}