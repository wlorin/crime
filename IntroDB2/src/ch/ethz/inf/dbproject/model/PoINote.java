package ch.ethz.inf.dbproject.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import ch.ethz.inf.dbproject.model.simpleDatabase.Tuple;

public class PoINote extends Note {
	final int poiId;
	final private int id;
	
	public PoINote(ResultSet rs) throws SQLException {
		super(rs);
		poiId = rs.getInt("PoIId");
		id = rs.getInt("PoINoteId");
	}
	
	public PoINote(Tuple t) throws Exception {
		super(t);
		poiId = t.getInt("PoIId");
		id = t.getInt("PoINoteId");
	}

	public int getId() {
		return id;
	}

	public int getPoiId() {
		return poiId;
	}
}
