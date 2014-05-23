package ch.ethz.inf.dbproject.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import ch.ethz.inf.dbproject.model.meta.Entity;
import ch.ethz.inf.dbproject.model.simpleDatabase.TupleSchema;

public class Suspect extends Entity {
	final int caseId;
	final int poiId;

	public Suspect(final int caseId, final int poiId) {
		this.poiId = poiId;
		this.caseId= caseId;
	}
	
	public Suspect(ResultSet rs) throws SQLException {
		caseId = rs.getInt("CaseId");
		poiId = rs.getInt("PoIId");
	}

	public int getCaseId() {
		return caseId;
	}
	public int getPoIId() {
		return poiId;
	}
}
