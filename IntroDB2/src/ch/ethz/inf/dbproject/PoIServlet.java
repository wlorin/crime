package ch.ethz.inf.dbproject;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ch.ethz.inf.dbproject.model.DatastoreInterfaceSimpleDatabase;
import ch.ethz.inf.dbproject.model.PoI;
import ch.ethz.inf.dbproject.util.UserManagement;
import ch.ethz.inf.dbproject.util.html.BeanTableHelper;

/**
 * Servlet implementation class of PoI list page
 */
@WebServlet(description = "Persons of Interest", urlPatterns = { "/PoI" })
public final class PoIServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private final DatastoreInterfaceSimpleDatabase dbInterface = new DatastoreInterfaceSimpleDatabase();

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public PoIServlet() {
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
		final BeanTableHelper<PoI> table = new BeanTableHelper<PoI>(
				"poI" 		/* The table html id property */,
				"casesTable" /* The table html class property */,
				PoI.class 	/* The class of the objects (rows) that will be displayed */
		);

		// Add columns to the new table
		
		final String action = request.getParameter("action");
		if ("delete".equals(action)) {
			final String idString = request.getParameter("id");
			Integer id = Integer.valueOf(idString);
			dbInterface.deletePoI(id);
		}

		table.addBeanColumn("Name", "name");
		table.addBeanColumn("Birthdate", "birthdate");

//		/*
//		 * Column 4: This is a special column. It adds a link to view the
//		 * Project. We need to pass the case identifier to the url.
//		 */
		table.addLinkColumn("Convictions"	/* The header. We will leave it empty */,
				"View Convictions" 	/* What should be displayed in every row */,
				"Conviction?id=" 	/* This is the base url. The final url will be composed from the concatenation of this and the parameter below */, 
				"id" 			/* For every case displayed, the ID will be retrieved and will be attached to the url base above */);
        
		
		table.addLinkColumn("Details"	/* The header. We will leave it empty */,
				"View Person" 	/* What should be displayed in every row */,
				"PoIDetail?id=" 	/* This is the base url. The final url will be composed from the concatenation of this and the parameter below */, 
				"id" 			/* For every case displayed, the ID will be retrieved and will be attached to the url base above */);
		
		
		if (UserManagement.isUserLoggedIn(session)) {
			table.addLinkColumn("Delete PoI", "Delete", "PoI?action=delete&id=","id");
		}
		
		
		// Pass the table to the session. This will allow the respective jsp page to display the table.
		session.setAttribute("poI", table);

		table.addObjects(this.dbInterface.getAll(PoI.class));

		this.getServletContext().getRequestDispatcher("/PoI.jsp").forward(request, response);
	}
}
