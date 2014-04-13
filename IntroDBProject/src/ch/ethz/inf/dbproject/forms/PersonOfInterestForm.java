package ch.ethz.inf.dbproject.forms;

import static ch.ethz.inf.dbproject.util.Constant.WEB_ROOT;

import java.io.IOException;
import java.sql.Date;
import java.util.Arrays;
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
import ch.ethz.inf.dbproject.forms.fields.StringField;
import ch.ethz.inf.dbproject.model.DatastoreInterface;
import ch.ethz.inf.dbproject.model.PoI;
import ch.ethz.inf.dbproject.util.UserManagement;


public class PersonOfInterestForm extends Form<PoI> {

	final String name = "Name";
	final String date = "Birthdate";
	
	DateField fieldBirthdate = new DateField(date, true);

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<Field> newFormFields() {
		return (List<Field>) (List) Arrays.asList(new Field[]{
				new StringField(name, false),
				fieldBirthdate
		});
	}
	
	@Override
	protected PoI processNewForm(List<Field> fields, HashMap<String, String> values, HttpSession _) {
		requireNotEmpty(name, values);
		
		String name = StringUtils.strip(values.get(this.name));
		Date birthdate = fieldBirthdate.parse(values);

		return new DatastoreInterface().insertPoI(name, birthdate);
	}

	@Override
	public List<Field> editFormFields() {
		throw new UnsupportedOperationException("Cannot edit PoI");
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
		throw new UnsupportedOperationException("Cannot edit PoI");
	}

	@Override
	protected void onNewSuccess(PoI result, ServletContext servletContext, HttpServletRequest request,
			HttpServletResponse response, HttpSession session) throws ServletException, IOException {
		session.setAttribute(UserManagement.SESSION_USER, result);
		response.sendRedirect(WEB_ROOT + "PoIDetail?id=" + result.getId());
	}
}
