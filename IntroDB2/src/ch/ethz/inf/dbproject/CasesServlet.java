package ch.ethz.inf.dbproject;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ch.ethz.inf.dbproject.forms.CaseForm;
import ch.ethz.inf.dbproject.model.Case;
import ch.ethz.inf.dbproject.model.DatastoreInterfaceSimpleDatabase;
import ch.ethz.inf.dbproject.util.UserManagement;
import ch.ethz.inf.dbproject.util.html.BeanTableHelper;

/**
 * Servlet implementation class of Case list page
 */
@WebServlet(description = "The home page of the project", urlPatterns = { "/Cases" })
public final class CasesServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private final DatastoreInterfaceSimpleDatabase dbInterface = new DatastoreInterfaceSimpleDatabase();

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public CasesServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected final void doGet(final HttpServletRequest request, final HttpServletResponse response) 
			throws ServletException, IOException {

		final HttpSession session = request.getSession(true);

		/*******************************************************
		 * Construct a table to present all our results
		 *******************************************************/
		final BeanTableHelper<Case> table = new BeanTableHelper<Case>(
				"cases" 		/* The table html id property */,
				"casesTable" /* The table html class property */,
				Case.class 	/* The class of the objects (rows) that will be displayed */
		);
		
		final String action = request.getParameter("action");
		if ("delete".equals(action)) {
			final String idString = request.getParameter("id");
			Integer id = Integer.valueOf(idString);
			dbInterface.deleteCase(id);
		}

		// Add columns to the new table

		table.addBeanColumn("Name", "name");
		table.addBeanColumn("Case Description", "crime");

		/*
		 * Columns 2 & 3: Some random fields. These should be replaced by i.e. funding progress, or time remaining
		 */
		table.addBeanColumn("Location", "location");
		table.addBeanColumn("Status", "status");
		table.addBeanColumn("Date", "date");
		table.addBeanColumn("Time", "time");

		/*
		 * Column 4: This is a special column. It adds a link to view the
		 * Project. We need to pass the case identifier to the url.
		 */
		table.addLinkColumn(""	/* The header. We will leave it empty */,
				"View Case" 	/* What should be displayed in every row */,
				"Case?id=" 	/* This is the base url. The final url will be composed from the concatenation of this and the parameter below */, 
				"id" 			/* For every case displayed, the ID will be retrieved and will be attached to the url base above */);
		
		/*
		 * Column 5: List of Suspects
		 */
		table.addLinkColumn("Suspects"	/* The header. We will leave it empty */,
				"View Suspects" 	/* What should be displayed in every row */, 
				"Suspect?CaseId=" 	/* This is the base url. The final url will be composed from the concatenation of this and the parameter below */, 
				"id" 			/* For every case displayed, the ID will be retrieved and will be attached to the url base above */);
        
		// Pass the table to the session. This will allow the respective jsp page to display the table.
		session.setAttribute("cases", table);

		// The filter parameter defines what to show on the Projects page
		final String filter = request.getParameter("filter");
		final String strCrimeId = request.getParameter("crimeId");

		if (UserManagement.isUserLoggedIn(session)) {
			table.addLinkColumn("Delete Case", "Delete", "Cases?action=delete&id=", "id");
			request.setAttribute("formNewCase", new CaseForm().generateNewForm());
		}
		
		if (filter == null && strCrimeId == null) {

			// If no filter is specified, then we display all the cases!
			table.addObjects(this.dbInterface.getAll(Case.class));

		} 
		else if (strCrimeId != null) {
			final int crimeId = Integer.valueOf(strCrimeId);
			table.addObjects(dbInterface.getCasesByCrime(crimeId));
			
		} 
		else if (filter != null) {
			if(filter.equals("open")) {
				table.addObjects(this.dbInterface.getByStatus("open"));
			} 
			else if (filter.equals("closed")) {
				table.addObjects(this.dbInterface.getByStatus("closed"));
			} 
			else if (filter.equals("recent")) {
				table.addObjects(this.dbInterface.getMostRecentCases(5));
			}
			else if (filter.equals("oldest")) {
				table.addObjects(this.dbInterface.getOldestUnsolvedCases(2));
			}
		} 
		else {
			throw new RuntimeException("Code should not be reachable!");
		}

		// Finally, proceed to the Projects.jsp page which will render the Projects
		this.getServletContext().getRequestDispatcher("/Cases.jsp").forward(request, response);
	}
}
