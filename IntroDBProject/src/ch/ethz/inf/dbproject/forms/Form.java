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

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;

import ch.ethz.inf.dbproject.forms.fields.Field;

public abstract class Form {
	public static String FIELD_TARGET_CLASS = "__targetClass";
	public static String FIELD_ACTION = "__action";
	
	public static String SESSION_FORM_FAIL = "__onFormFailMsg";
	public static String SESSION_FORM_FAIL_MSG = "__onFormFailHtml";
	
	protected abstract String getMethod();
	
	protected String generateForm(String title, String buttonCaption, List<Field> fields, String action, HashMap<String, String> values) {
		StringBuilder html = new StringBuilder();
		html.append("<h2>" + title + "</h2>");
		appendFormHeader(action, html);
		html.append("<table>");
		for (Field field : fields) {
			html.append(field.getHtmlCode(values.get(field.getDisplayName())));
		}
		html.append("<tr><th colspan=2>" + submitButton(buttonCaption) + "</th></tr>");
		html.append("</table>");
		appendFormFooter(html);
		return html.toString();
	}

	protected String submitButton(String buttonCaption) {
		return "<input type=submit value=\"" + StringEscapeUtils.escapeHtml4(buttonCaption) +"\">";
	}

	protected void appendFormFooter(StringBuilder html) {
		html.append("</form>");
	}

	protected void appendFormHeader(String action, StringBuilder html) {
		html.append("<form action='" + WEB_ROOT + "PostHandlerServlet' method='" + getMethod() + "'>");
		html.append(String.format("<input type=hidden name=" + FIELD_TARGET_CLASS + " value=%s />", this.getClass().getName()));
		html.append("<input type=hidden name=" + FIELD_ACTION + " value=" + action +" />");
	}
	
	protected void require(String string, boolean conditionSatisfied) {
		if (!conditionSatisfied) {
			throw new UserInputException(string);
		}
	}
	protected void requireNotEmpty(String fieldName, HashMap<String, String> values) {
		require(fieldName + " cannot be empty", StringUtils.isNotBlank(values.get(fieldName)));
	}

	public abstract void dispatch(String action, ServletContext servletContext,
			HttpServletRequest request, HttpServletResponse response,
			HttpSession session) throws ServletException, IOException;
}


