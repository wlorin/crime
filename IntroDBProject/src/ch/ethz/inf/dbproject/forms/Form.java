package ch.ethz.inf.dbproject.forms;

import static ch.ethz.inf.dbproject.util.Constant.WEB_ROOT;

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
	
	protected abstract String getMethod();
	
	public String generateForm(String title, List<Field> fields, HashMap<String, String> values) {
		StringBuilder html = new StringBuilder();
		html.append("<h2>" + title + "</h2>");
		html.append("<form action='" + WEB_ROOT + "PostHandlerServlet' method='" + getMethod() + "'>");
		html.append("<table>");
		html.append(String.format("<input type=hidden name=" + FIELD_TARGET_CLASS + " value=%s />", this.getClass().getName()));
		html.append("<input type=hidden name=" + FIELD_NEW_OR_EDIT + " value=new />");
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


