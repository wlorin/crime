package ch.ethz.inf.dbproject.forms;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ch.ethz.inf.dbproject.forms.fields.Field;

public abstract class CreationForm<NewResult> extends Form {
	protected abstract List<Field> newFormFields();
	
	protected List<Field> editFormFields() {
		return newFormFields();
	}
	
	protected abstract String getNewFormTitle();
	protected abstract String getEditFormTitle();
	
	public void processNewForm(ServletContext servletContext, HttpServletRequest request, HttpServletResponse response, HttpSession session) throws ServletException, IOException {
		HashMap<String, String> values = new HashMap<>();
		List<Field> newFormFields = newFormFields();
		for (Field field : newFormFields) {
			values.put(field.getDisplayName(), request.getParameter(field.getDisplayName()));
		}
		
		try {
			validateFields(newFormFields, values);
			NewResult result = processNewForm(newFormFields, values, session);
			onNewSuccess(result, servletContext, request, response, session);
		}
		catch(UserInputException e) {
			session.setAttribute(SESSION_FORM_FAIL_MSG, e.getMessage());
			session.setAttribute(SESSION_FORM_FAIL, generateNewForm(values));
			servletContext.getRequestDispatcher("/FormError.jsp").forward(request, response);
		}
	}
	
	public void processEditForm(ServletContext servletContext, HttpServletRequest request, HttpServletResponse response, HttpSession session) throws ServletException, IOException {
		HashMap<String, String> values = new HashMap<>();
		List<Field> editFormFields = editFormFields();
		for (Field field : editFormFields) {
			values.put(field.getDisplayName(), request.getParameter(field.getDisplayName()));
		}
		
		try {
			validateFields(editFormFields, values);
			NewResult result = processEditForm(editFormFields, values, session);
			onEditSuccess(result, servletContext, request, response, session);
		}
		catch(UserInputException e) {
			session.setAttribute(SESSION_FORM_FAIL_MSG, e.getMessage());
			session.setAttribute(SESSION_FORM_FAIL, generateEditForm(values));
			servletContext.getRequestDispatcher("/FormError.jsp").forward(request, response);
		}
	}
	
	protected abstract void onNewSuccess(NewResult result, ServletContext servletContext, HttpServletRequest request,
			HttpServletResponse response, HttpSession session) throws ServletException, IOException;
	protected abstract void onEditSuccess(NewResult result, ServletContext servletContext, HttpServletRequest request,
			HttpServletResponse response, HttpSession session) throws ServletException, IOException;

	protected abstract NewResult processNewForm(List<Field> fields, HashMap<String, String> values, HttpSession session);
	protected abstract NewResult processEditForm(List<Field> fields, HashMap<String, String> values, HttpSession session);
	
	public String generateNewForm() {
		return generateNewForm(new HashMap<String, String>());
	}
	
	private void validateFields(List<Field> fields, HashMap<String, String> values) {
		// TODO Auto-generated method stub
		for (Field field : fields) {
			field.validate(values);
		}
	}
	
	public String generateNewFormWith(String key, String value) {
		HashMap<String, String> values = new HashMap<>();
		values.put(key, value);
		return generateNewForm(values);
	}
	
	public String generateEditForm(HashMap<String, String> values, String buttonStr) {
		return generateForm(getEditFormTitle(), buttonStr, editFormFields(), "edit", values);
	}
	public String generateNewForm(HashMap<String, String> values, String buttonStr) {
		return generateForm(getNewFormTitle(), buttonStr, newFormFields(), "new", values);
	}
	public String generateNewForm(HashMap<String, String> values) {
		return generateNewForm(values, "Create");
	}
	public String generateEditForm(HashMap<String, String> values) {
		return generateEditForm(values, "Edit");
	}
	
	@Override
	public void dispatch(String action, ServletContext servletContext,
			HttpServletRequest request, HttpServletResponse response,
			HttpSession session) throws ServletException, IOException {
		// TODO Auto-generated method stub
		if ("new".equals(action)) {
			processNewForm(servletContext, request, response, request.getSession());
		}
		else if ("edit".equals(action)) {
			processEditForm(servletContext, request, response, request.getSession());
		}
		else {
			
		}
	}
}


