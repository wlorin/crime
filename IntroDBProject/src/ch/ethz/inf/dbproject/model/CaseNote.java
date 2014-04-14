package ch.ethz.inf.dbproject.model;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CaseNote extends Note {
	
	final int caseId;

	public CaseNote(final String username, final String comment, final int caseId) {
		super(username, comment);
		this.caseId = caseId;
	}
	
	public CaseNote(ResultSet rs) throws SQLException {
		super(rs);
		caseId = rs.getInt("CaseId");
	}

	public int getCaseId() {
		return caseId;
	}
}
