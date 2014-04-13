package ch.ethz.inf.dbproject.model;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PoINote extends Note {
	
	final int poiId;

	public PoINote(final String username, final String comment, final int poiId) {
		super(username, comment);
		this.poiId = poiId;
	}
	
	public PoINote(ResultSet rs) throws SQLException {
		super(rs);
		poiId = rs.getInt("PoIId");
	}

	public int getCaseId() {
		return poiId;
	}
}