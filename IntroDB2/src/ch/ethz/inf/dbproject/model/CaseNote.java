package ch.ethz.inf.dbproject.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import ch.ethz.inf.dbproject.model.simpleDatabase.Tuple;

public class CaseNote extends Note {
	
	final int caseId;
	
	public CaseNote(ResultSet rs) throws SQLException {
		super(rs);
		caseId = rs.getInt("CaseId");
	}
	
	public CaseNote(Tuple t) throws Exception {
		super(t);
		caseId = t.getInt("CaseId");
	}

	public int getId() {
		return caseId;
	}
}
