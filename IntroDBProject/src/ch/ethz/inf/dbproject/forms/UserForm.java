package ch.ethz.inf.dbproject.forms;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;

import ch.ethz.inf.dbproject.forms.fields.Field;
import ch.ethz.inf.dbproject.forms.fields.PasswordField;
import ch.ethz.inf.dbproject.forms.fields.StringField;
import ch.ethz.inf.dbproject.model.DatastoreInterface;
import ch.ethz.inf.dbproject.model.User;
import ch.ethz.inf.dbproject.util.UserManagement;

public class UserForm extends Form<User> {

	final String name = "Name";
	final String password = "Password";
	final String confirmation = "Confirmation";

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<Field> newFormFields() {
		return (List<Field>) (List) Arrays.asList(
				new Field[]{
				new StringField(name),
				new PasswordField(password),
				new PasswordField(confirmation)}
		);
	}
	
	@Override
	protected User processNewForm(List<Field> fields, HashMap<String, String> values) {
		requireNotEmpty(name, values);
		require("Password and confirmation don't match", Objects.equals(values.get(password), values.get(confirmation)));
		
		String name = StringUtils.strip(values.get(this.name));
		String password = StringUtils.strip(values.get(this.password));

		return new DatastoreInterface().insertUser(name, password);
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
		return "Create new User";
	}

	@Override
	protected String getEditFormTitle() {
		throw new UnsupportedOperationException("Cannot edit User");
	}

	@Override
	protected void onNewSuccess(User result, ServletContext servletContext, HttpServletRequest request,
			HttpServletResponse response, HttpSession session) throws ServletException, IOException {
		session.setAttribute(UserManagement.SESSION_USER, result);
		response.sendRedirect("/IntroDBProject/User");
	}
}
