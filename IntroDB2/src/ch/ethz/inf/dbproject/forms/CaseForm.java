package ch.ethz.inf.dbproject.forms;

import static ch.ethz.inf.dbproject.util.Constant.WEB_ROOT;

import java.io.IOException;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
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
import ch.ethz.inf.dbproject.forms.fields.CrimeField;
import ch.ethz.inf.dbproject.forms.fields.DateField;
import ch.ethz.inf.dbproject.forms.fields.Field;
import ch.ethz.inf.dbproject.forms.fields.HiddenField;
import ch.ethz.inf.dbproject.forms.fields.InputField;
import ch.ethz.inf.dbproject.forms.fields.StringField;
import ch.ethz.inf.dbproject.forms.fields.TimeField;
import ch.ethz.inf.dbproject.model.Case;
import ch.ethz.inf.dbproject.model.DatastoreInterfaceMySQL;


public class CaseForm extends CreationForm<Case> {

	public static String name = "Name";
	public static  String location = "Location";
	public static  String status = "Status";
	public static  String crime = "Crime";
	public static  String date = "Date";
	public static  String time = "Time";
	public static  String id = "id";

	final String confirmation = "Confirmation";
	private CaseStatusField caseStatusField = new CaseStatusField(status);
	private CrimeField categoryField = new CrimeField(crime);
	
	private StringField fieldName = new StringField(name, false);
	private StringField fieldLocation = new StringField(location, true);
	private InputField<Date> fieldDate = new DateField(date, true);
	private TimeField fieldTime = new TimeField(time, true);
	
	private HiddenField fieldId = new HiddenField(id, false);

	
	@Override
	public List<Field> newFormFields() {
		List<Field> bla = new ArrayList<Field>();
		bla.add(fieldName);
		bla.add(fieldLocation);
		bla.add(caseStatusField);
		bla.add(categoryField);
		bla.add(fieldDate);
		bla.add(fieldTime);
		return bla;
	}
	
	@Override
	protected Case processNewForm(List<Field> fields, HashMap<String, String> values, HttpSession session) {
		requireNotEmpty(name, values);
		
		String name = StringUtils.strip(values.get(CaseForm.name));
		String location = StringUtils.strip(fieldLocation.parse(values));
		String state = StringUtils.strip(values.get(status));
		int crimeId = Integer.valueOf(values.get(crime));
		Date date = fieldDate.parse(values);
		Time time = fieldTime.parse(values);
		
		return new DatastoreInterfaceMySQL().insertCase(name, state, crimeId, location, date, time);
	}
	
	protected Case processEditForm(List<Field> fields, HashMap<String, String> values, HttpSession session) {
		requireNotEmpty(name, values);
		
		String name = StringUtils.strip(values.get(CaseForm.name));
		String location = StringUtils.strip(fieldLocation.parse(values));
		String state = StringUtils.strip(values.get(status));
		int crimeId = Integer.valueOf(values.get(crime));
		Date date = fieldDate.parse(values);
		Time time = fieldTime.parse(values);
		int caseId = Integer.valueOf(values.get(id));
		
		return new DatastoreInterfaceMySQL().updateCase(caseId, name, state, crimeId, location, date, time);
	}

	@Override
	public List<Field> editFormFields() {
		List<Field> res = newFormFields();
		res.add(fieldId);
		return res;
		
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
		return "Edit Case";
	}


	@Override
	protected void onNewSuccess(Case result, ServletContext servletContext,
			HttpServletRequest request, HttpServletResponse response,
			HttpSession session) throws ServletException, IOException {
		response.sendRedirect(WEB_ROOT + "Case?id=" + result.getId());
	}
	protected void onEditSuccess(Case result, ServletContext servletContext,
			HttpServletRequest request, HttpServletResponse response,
			HttpSession session) throws ServletException, IOException {
		response.sendRedirect(WEB_ROOT + "Case?id=" + result.getId());
	}
}
