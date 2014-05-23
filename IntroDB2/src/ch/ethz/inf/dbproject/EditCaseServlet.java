package ch.ethz.inf.dbproject;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ch.ethz.inf.dbproject.forms.CaseForm;
import ch.ethz.inf.dbproject.model.Case;
import ch.ethz.inf.dbproject.model.DatastoreInterfaceSimpleDatabase;

@WebServlet(description = "Edits a specific case.", urlPatterns = { "/EditCase" })
public final class EditCaseServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private final DatastoreInterfaceSimpleDatabase dbInterface = new DatastoreInterfaceSimpleDatabase();

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public EditCaseServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected final void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {

		final HttpSession session = request.getSession(true);
		final String idString = request.getParameter("id");
		if (idString == null) {
			this.getServletContext().getRequestDispatcher("/Cases").forward(request, response);
			return;
		}
		Long id = Long.valueOf(idString);
		Case aCase = dbInterface.getById(id, Case.class);
		session.setAttribute("caseName", aCase.getName());
		HashMap<String, String> initialValues = new HashMap<String, String>();
		initialValues.put(CaseForm.crime, aCase.getCrime());
		initialValues.put(CaseForm.date, (aCase.getDate() == null ? "" : aCase.getDate().toString()));
		initialValues.put(CaseForm.id, String.valueOf(aCase.getId()));
		initialValues.put(CaseForm.location, aCase.getLocation());
		initialValues.put(CaseForm.name, aCase.getName());
		initialValues.put(CaseForm.status, aCase.getStatus());
		initialValues.put(CaseForm.time, (aCase.getTime() == null ? "" : aCase.getTime().toString().substring(0, 5))); //pardon by dirty hack :(
		request.setAttribute("initialValues", initialValues);
		this.getServletContext().getRequestDispatcher("/EditCase.jsp").forward(request, response);
		
	}
}