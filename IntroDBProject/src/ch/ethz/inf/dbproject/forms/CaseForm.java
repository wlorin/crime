package ch.ethz.inf.dbproject.forms;

import java.io.IOException;
import java.sql.Date;
import java.sql.Time;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;

import ch.ethz.inf.dbproject.forms.fields.CaseStatusField;
import ch.ethz.inf.dbproject.forms.fields.DateField;
import ch.ethz.inf.dbproject.forms.fields.Field;
import ch.ethz.inf.dbproject.forms.fields.InputField;
import ch.ethz.inf.dbproject.forms.fields.StringField;
import ch.ethz.inf.dbproject.forms.fields.TimeField;
import ch.ethz.inf.dbproject.model.Case;
import ch.ethz.inf.dbproject.model.DatastoreInterface;


public class CaseForm extends Form<Case> {

	final String name = "Name";
	final String location = "Location";
	final String status = "Status";
	final String date = "Date";
	final String time = "Time";

	final String confirmation = "Confirmation";
	private CaseStatusField caseStatusField = new CaseStatusField(status);
	
	private StringField fieldName = new StringField(name, false);
	private StringField fieldLocation = new StringField(location, true);
	private InputField<Date> fieldDate = new DateField(date, true);
	private TimeField fieldTime = new TimeField(time, true);

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<Field> newFormFields() {
		return (List<Field>) (List) Arrays.asList(
				new Field[]{
				fieldName,
				fieldLocation,
				caseStatusField,
				fieldDate,
				fieldTime,
		});
	}
	
	@Override
	protected Case processNewForm(List<Field> fields, HashMap<String, String> values) {
		requireNotEmpty(name, values);
		
		String name = StringUtils.strip(values.get(this.name));
		String location = StringUtils.strip(fieldLocation.parse(values));
		String state = StringUtils.strip(values.get(this.status));
		Date date = null; //StringUtils.strip(values.get(this.date));
		Time time = null; //StringUtils.strip(values.get(this.time));
		

		return new DatastoreInterface().insertCase(name, state, location, date, time);
	}

	@Override
	public List<Field> editFormFields() {
		throw new UnsupportedOperationException("Cannot edit User");
	}

	@Override
	protected String getMethod() {
		return "POST";
	}

	@Override
	protected String getNewFormTitle() {
		return "Create new Case";
	}

	@Override
	protected String getEditFormTitle() {
		throw new UnsupportedOperationException("Cannot edit User");
	}


	@Override
	protected void onNewSuccess(Case result, ServletContext servletContext,
			HttpServletRequest request, HttpServletResponse response,
			HttpSession session) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
	}
}
