package ch.ethz.inf.dbproject.forms;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ch.ethz.inf.dbproject.model.DatastoreInterfaceSimpleDatabase;
import static ch.ethz.inf.dbproject.util.Constant.WEB_ROOT;

public class OpenCloseButton extends ButtonForm {

	public static final String ACTION_CLOSE = "close";
	public static final String ACTION_OPEN = "open";
	
	@Override
	public void dispatch(String action, ServletContext servletContext,
			HttpServletRequest request, HttpServletResponse response,
			HttpSession session) throws ServletException, IOException 
	{
		int caseId = Integer.valueOf(request.getParameter(ButtonForm.PARAMETER_FIELD));
		if (ACTION_OPEN.equals(action)) {
			new DatastoreInterfaceSimpleDatabase().openCase(caseId);
		}
		else if (ACTION_CLOSE.equals(action)){
			new DatastoreInterfaceSimpleDatabase().closeCase(caseId);
		}
		else {
			throw new UnsupportedOperationException("Action " + action + " not supported");
		}
		response.sendRedirect(WEB_ROOT + "Case?id=" + caseId);
	}

}
