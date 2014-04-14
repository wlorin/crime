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
import ch.ethz.inf.dbproject.model.DatastoreInterface;
import ch.ethz.inf.dbproject.util.UserManagement;
import ch.ethz.inf.dbproject.util.html.BeanTableHelper;

/**
 * Servlet implementation class of Case list page
 */
@WebServlet(description = "The home page of the project", urlPatterns = { "/Cases" })
public final class CasesServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private final DatastoreInterface dbInterface = new DatastoreInterface();

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
				"caseId" 			/* For every case displayed, the ID will be retrieved and will be attached to the url base above */);
		
		/*
		 * Column 5: List of Suspects
		 */
		table.addLinkColumn("Suspects"	/* The header. We will leave it empty */,
				"View Suspects" 	/* What should be displayed in every row */,
				"Suspect?id=" 	/* This is the base url. The final url will be composed from the concatenation of this and the parameter below */, 
				"caseId" 			/* For every case displayed, the ID will be retrieved and will be attached to the url base above */);
        
		// Pass the table to the session. This will allow the respective jsp page to display the table.
		session.setAttribute("cases", table);

		// The filter parameter defines what to show on the Projects page
		final String filter = request.getParameter("filter");
		final String category = request.getParameter("category");

		if (UserManagement.isUserLoggedIn(session)) {
			request.setAttribute("formNewCase", new CaseForm().generateNewForm());
		}
		
		if (filter == null && category == null) {

			// If no filter is specified, then we display all the cases!
			table.addObjects(this.dbInterface.getAll(Case.class));

		} else if (category != null) {
			table.addObjects(dbInterface.getProjectsByCategory(category));
			/*
			if(category.equals("personal")) {

				table.addObjects(this.dbInterface.getProjectsByCategory("Koerperverletzung"));
				table.addObjects(this.dbInterface.getProjectsByCategory("Mord"));
			
			} else if (category.equals("property")) {
				table.addObjects(this.dbInterface.getProjectsByCategory("Bankraub"));
				table.addObjects(this.dbInterface.getProjectsByCategory("Diebstahl"));
				table.addObjects(this.dbInterface.getProjectsByCategory("Einbruch"));

			} 
			else if (category.equals("assault")) {
				table.addObjects(this.dbInterface.getProjectsByCategory("Koerperverletzung"));

			}			
			else if (category.equals("murder")) {
				table.addObjects(this.dbInterface.getProjectsByCategory("Mord"));
			}
			else if (category.equals("theft")) {
				table.addObjects(this.dbInterface.getProjectsByCategory("Diebstahl"));
			}
			else if (category.equals("burglary")) {
				table.addObjects(this.dbInterface.getProjectsByCategory("Einbruch"));
			}
			else if (category.equals("robbery")) {
				table.addObjects(this.dbInterface.getProjectsByCategory("Bankraub"));
			}
			else {
				table.addObjects(this.dbInterface.getProjectsWithoutCategory());
			}
			*/
			
		} else if (filter != null) {
		
			if(filter.equals("open")) {

				table.addObjects(this.dbInterface.getByStatus("open"));

			} else if (filter.equals("closed")) {

				table.addObjects(this.dbInterface.getByStatus("closed"));

			} else if (filter.equals("recent")) {

				table.addObjects(this.dbInterface.getMostRecentCases(5));

			}
			
			else if (filter.equals("oldest")) {

				table.addObjects(this.dbInterface.getOldestUnsolvedCases(2));

			}
			
		} else {
			throw new RuntimeException("Code should not be reachable!");
		}

		// Finally, proceed to the Projects.jsp page which will render the Projects
		this.getServletContext().getRequestDispatcher("/Cases.jsp").forward(request, response);
	}
}