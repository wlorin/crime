package ch.ethz.inf.dbproject.model;

import java.util.Date;

import ch.ethz.inf.dbproject.model.meta.Entity;

/**
 * Object that represents a conviction.
 */
public class Conviction implements Entity {

	private final Date date;
	private final Date endDate;
	private final String type;

	public Date getDate() {
		return date;
	}

	public Date getEndDate() {
		return endDate;
	}

	public String getType() {
		return type;
	}

	
	public Conviction(final Date date, final Date endDate, final String type) {
		this.date = date;
		this.endDate = endDate;
		this.type = type;
	}

		
}
