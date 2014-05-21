package ch.ethz.inf.dbproject.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import ch.ethz.inf.dbproject.model.meta.Entity;
import ch.ethz.inf.dbproject.model.meta.TableName;

/**
 * Object that represents a conviction.
 */
@TableName("Convicted")
public class Conviction implements Entity {

	private final int poIId;
	private final int caseId;
	private final int crimeId;
	private final Date date;
	private final String sentence;

	public Conviction(ResultSet rs) throws SQLException {
		this.caseId = rs.getInt("caseId");
		this.crimeId = rs.getInt("crimeId");
		this.poIId = rs.getInt("poIId");
		this.date = rs.getDate("date");
		this.sentence = rs.getString("sentence");
	}
	
	public Date getDate() {
		return date;
	}

    public int getPoIId(){
    	return poIId;
    }
    
    public int getCaseId(){
    	return caseId;
    }
    
    public int getCrimeId(){
    	return crimeId;
    }

	public String getSentence() {
		return sentence;
	}
	
	public String getCasename() {
		final DatastoreInterfaceMySQL dbInterface = new DatastoreInterfaceMySQL();
		return dbInterface.getCasenameById(crimeId);
	}
	
	public String getCrime() {
		final DatastoreInterfaceMySQL dbInterface = new DatastoreInterfaceMySQL();
		return dbInterface.getCrimeById(crimeId);
	}
	
	public Conviction(final Date date, final int poIId, final int caseId, final int crimeId, final String sentence) {
		this.date = date;
		this.sentence = sentence;
		this.poIId = poIId;
		this.caseId = caseId;
		this.crimeId = crimeId;
	}

		
}
