package ch.ethz.inf.dbproject.forms;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;

import ch.ethz.inf.dbproject.forms.fields.Field;

public abstract class Form<NewResult> {
	public static String FIELD_TARGET_CLASS = "__targetClass";
	public static String FIELD_NEW_OR_EDIT = "__newOrEdit";
	
	public static String SESSION_FORM_FAIL = "__onFormFailMsg";
	public static String SESSION_FORM_FAIL_MSG = "__onFormFailHtml";
	protected abstract List<Field> newFormFields();
	
	protected List<Field> editFormFields() {
		return newFormFields();
	}
	
	protected abstract String getNewFormTitle();
	protected abstract String getEditFormTitle();
	
	protected abstract String getMethod();
	
	public void processNewForm(ServletContext servletContext, HttpServletRequest request, HttpServletResponse response, HttpSession session) throws ServletException, IOException {
		HashMap<String, String> values = new HashMap<>();
		List<Field> newFormFields = newFormFields();
		for (Field field : newFormFields) {
			values.put(field.getDisplayName(), request.getParameter(field.getDisplayName()));
		}
		
		try {
			NewResult result = processNewForm(newFormFields, values);
			onNewSuccess(result, servletContext, request, response, session);
		}
		catch(UserInputException e) {
			session.setAttribute(SESSION_FORM_FAIL_MSG, e.getMessage());
			session.setAttribute(SESSION_FORM_FAIL, generateNewForm(values));
			servletContext.getRequestDispatcher("/FormError.jsp").forward(request, response);
		}
	}
	
	protected abstract void onNewSuccess(NewResult result, ServletContext servletContext, HttpServletRequest request,
			HttpServletResponse response, HttpSession session);

	protected abstract NewResult processNewForm(List<Field> fields, HashMap<String, String> values);
	
	public String generateNewForm() {
		return generateNewForm(new HashMap<String, String>());
	}
	
	public String generateNewForm(HashMap<String, String> values) {
		StringBuilder html = new StringBuilder();
		html.append("<h2>" + getNewFormTitle() + "</h2>");
		html.append("<form action='/IntroDBProject/PostHandlerServlet' method='" + getMethod() + "'>");
		html.append("<table>");
		html.append(String.format("<input type=hidden name=" + FIELD_TARGET_CLASS + " value=%s />", this.getClass().getName()));
		html.append("<input type=hidden name=" + FIELD_NEW_OR_EDIT + " value=new />");
		List<Field> fields = newFormFields();
		for (Field field : fields) {
			html.append(field.getHtmlCode(values.get(field.getDisplayName())));
		}
		html.append("<tr><th colspan=2><input type=submit value=Create></th></tr>");
		html.append("</table>");
		html.append("</form>");
		return html.toString();
	}
	
	protected void require(String string, boolean conditionSatisfied) {
		if (!conditionSatisfied) {
			throw new UserInputException(string);
		}
	}
	protected void requireNotEmpty(String fieldName, HashMap<String, String> values) {
		require(fieldName + " cannot be empty", StringUtils.isNotBlank(values.get(fieldName)));
	}
}


