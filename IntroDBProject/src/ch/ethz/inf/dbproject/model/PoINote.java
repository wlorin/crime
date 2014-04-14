package ch.ethz.inf.dbproject.model;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PoINote extends Note {
	
	final int poiId;
	final private int id;
	
	public PoINote(ResultSet rs) throws SQLException {
		super(rs);
		poiId = rs.getInt("PoIId");
		id = rs.getInt("PoINoteId");
	}

	public int getId() {
		return id;
	}

	public int getCaseId() {
		return poiId;
	}

	public int getPoiId() {
		return poiId;
	}
}
