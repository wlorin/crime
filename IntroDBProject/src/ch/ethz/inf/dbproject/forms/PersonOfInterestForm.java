package ch.ethz.inf.dbproject.forms;

import static ch.ethz.inf.dbproject.util.Constant.WEB_ROOT;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;

import ch.ethz.inf.dbproject.forms.fields.DateField;
import ch.ethz.inf.dbproject.forms.fields.Field;
import ch.ethz.inf.dbproject.forms.fields.HiddenField;
import ch.ethz.inf.dbproject.forms.fields.StringField;
import ch.ethz.inf.dbproject.model.DatastoreInterface;
import ch.ethz.inf.dbproject.model.PoI;


public class PersonOfInterestForm extends CreationForm<PoI> {

	public static final String name = "Name";
	public static final String date = "Birthdate";
	public static final String id = "id";
	
	DateField fieldBirthdate = new DateField(date, true);

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<Field> newFormFields() {
		List<Field> bla = new ArrayList<Field>();
		bla.add(new StringField(name, false));
		bla.add(fieldBirthdate);
		return bla;
	}
	
	@Override
	protected PoI processNewForm(List<Field> fields, HashMap<String, String> values, HttpSession _) {
		requireNotEmpty(name, values);
		
		String name = StringUtils.strip(values.get(PersonOfInterestForm.name));
		Date birthdate = fieldBirthdate.parse(values);

		return new DatastoreInterface().insertPoI(name, birthdate);
	}

	@Override
	public List<Field> editFormFields() {
		List<Field> bla = newFormFields();
		bla.add(new HiddenField(id, false));
		return bla;
	}

	@Override
	protected String getMethod() {
		return "POST";
	}

	@Override
	protected String getNewFormTitle() {
		return "Create new Person of Interest";
	}

	@Override
	protected String getEditFormTitle() {
		return "Edit Person of Interest";
	}

	@Override
	protected void onNewSuccess(PoI result, ServletContext servletContext, HttpServletRequest request,
			HttpServletResponse response, HttpSession session) throws ServletException, IOException {
		response.sendRedirect(WEB_ROOT + "PoIDetail?id=" + result.getId());
	}

	@Override
	protected void onEditSuccess(PoI result, ServletContext servletContext,
			HttpServletRequest request, HttpServletResponse response,
			HttpSession session) throws ServletException, IOException {
		response.sendRedirect(WEB_ROOT + "PoIDetail?id=" + result.getId());
		
	}

	@Override
	protected PoI processEditForm(List<Field> fields,
			HashMap<String, String> values, HttpSession session) {
		// requireNotEmpty(name, values);
		
		String name = StringUtils.strip(values.get(PersonOfInterestForm.name));
		Date birthdate = fieldBirthdate.parse(values);
		Long id = Long.valueOf(values.get(PersonOfInterestForm.id));

		return new DatastoreInterface().updatePoI(id, name, birthdate);
	}
}
