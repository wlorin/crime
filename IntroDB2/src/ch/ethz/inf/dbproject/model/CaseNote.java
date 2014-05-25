package ch.ethz.inf.dbproject.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import ch.ethz.inf.dbproject.model.simpleDatabase.Tuple;

public class CaseNote extends Note {
	
	final int caseId;
	final private int id;
	
	public CaseNote(ResultSet rs) throws SQLException {
		super(rs);
		caseId = rs.getInt("CaseId");
		id = rs.getInt("CaseNoteId");
		
	}
	
	public CaseNote(Tuple t) throws Exception {
		super(t);
		caseId = t.getInt("CaseId");
		id = t.getInt("CaseNoteId");
	}

	public int getCaseId() {
		return caseId;
	}
	
	public int getId() {
		return id;
	}
}
