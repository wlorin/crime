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
import ch.ethz.inf.dbproject.forms.PersonOfInterestForm;
import ch.ethz.inf.dbproject.model.Case;
import ch.ethz.inf.dbproject.model.DatastoreInterfaceMySQL;
import ch.ethz.inf.dbproject.model.PoI;

@WebServlet(description = "Edits a specific case.", urlPatterns = { "/EditPoI" })
public final class EditPoIServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private final DatastoreInterfaceMySQL dbInterface = new DatastoreInterfaceMySQL();

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public EditPoIServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected final void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {

		final HttpSession session = request.getSession(true);
		final String idString = request.getParameter("id");
		if (idString == null) {
			this.getServletContext().getRequestDispatcher("/PoI").forward(request, response);
			return;
		}
		Long id = Long.valueOf(idString);
		DatastoreInterfaceMySQL dbInterface = new DatastoreInterfaceMySQL();
		PoI poi = dbInterface.getById(id, PoI.class);
		session.setAttribute("poiName", poi.getName());
		
		HashMap<String, String> initialValues = new HashMap<String, String>();
		initialValues.put(PersonOfInterestForm.date, (poi.getBirthdate() == null ? "" : poi.getBirthdate().toString()));
		initialValues.put(PersonOfInterestForm.name, poi.getName());
		initialValues.put(PersonOfInterestForm.id, String.valueOf(poi.getId()));
		request.setAttribute("initialValues", initialValues);
		this.getServletContext().getRequestDispatcher("/EditPoI.jsp").forward(request, response);
		
	}
}