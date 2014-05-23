package ch.ethz.inf.dbproject.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import ch.ethz.inf.dbproject.model.simpleDatabase.Tuple;

public final class Convict extends PoI {
	
	
	final private int crimeId;
	final private String crime;
	final private Date convictionDate;
	final private int caseId;
	final private String sentence;
	public Convict(int caseId, int poiId, int crimeId, String crime, String name, Date birthDate, Date convictionDate, String sentence) {
		super(poiId, name, birthDate);
		this.caseId= caseId;
		this.crimeId = crimeId;
		this.crime = crime;
		this.convictionDate = convictionDate;
		this.sentence = sentence;
	}

	public Convict(ResultSet rs) throws SQLException {
		super(rs);
		crimeId = rs.getInt("CrimeId");
		caseId = rs.getInt("CaseId");
		crime = rs.getString("Crime");
		convictionDate = rs.getDate("ConvictionDate");
		sentence = rs.getString("Sentence");
	}
	public Convict(Tuple t) {
		super(t);
		crimeId = t.getInt("CrimeId");
		caseId = t.getInt("CaseId");
		crime = t.getString("Crime");
		convictionDate = t.getDate("ConvictionDate");
		sentence = t.getString("Sentence");
	}

	public int getCrimeId() {
		return crimeId;
	}

	public String getCrime() {
		return crime;
	}

	public int getCaseId() {
		return caseId;
	}
	
	public Date getConvictionDate() {
		return convictionDate;
	}
	public String getSentence() {
		return sentence;
	}
	
	public String getPoiCrime() {
		return getId() + "-" + crimeId;
	}
	
}
