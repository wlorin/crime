package ch.ethz.inf.dbproject.forms;

import static ch.ethz.inf.dbproject.util.Constant.WEB_ROOT;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;

import ch.ethz.inf.dbproject.forms.fields.Field;
import ch.ethz.inf.dbproject.forms.fields.StringField;
import ch.ethz.inf.dbproject.model.Crime;
import ch.ethz.inf.dbproject.model.DatastoreInterface;

public class CrimeForm extends CreationForm<Crime> {
	final String crime = "Crime";

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	protected List<Field> newFormFields() {
		return (List<Field>) (List) Arrays.asList(new Field[]{
				new StringField(crime, false)
		});
	}

	@Override
	protected String getNewFormTitle() {
		return "Enter new crime";
	}

	@Override
	protected String getEditFormTitle() {
		throw new UnsupportedOperationException("Cannot edit Conviction");
	}

	@Override
	protected void onNewSuccess(Crime result,
			ServletContext servletContext, HttpServletRequest request,
			HttpServletResponse response, HttpSession session)
			throws ServletException, IOException {
		response.sendRedirect(WEB_ROOT + "User");
		
	}

	@Override
	protected Crime processNewForm(List<Field> fields,
			HashMap<String, String> values, HttpSession session) {
		for (Entry<String, String> item : values.entrySet()) {
			System.out.println(item.getKey() + " => " + item.getValue());
		}
		requireNotEmpty(this.crime, values);
		
		String crimeName = StringUtils.strip(values.get(this.crime));
		return new DatastoreInterface().insertCrime(crimeName);
	}

	@Override
	protected String getMethod() {
		return "POST";
	}

	@Override
	protected void onEditSuccess(Crime result,
			ServletContext servletContext, HttpServletRequest request,
			HttpServletResponse response, HttpSession session)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected Crime processEditForm(List<Field> fields,
			HashMap<String, String> values, HttpSession session) {
		// TODO Auto-generated method stub
		return null;
	}

}
