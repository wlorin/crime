package ch.ethz.inf.dbproject;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ch.ethz.inf.dbproject.model.DatastoreInterface;
import ch.ethz.inf.dbproject.model.PoI;
import ch.ethz.inf.dbproject.util.html.BeanTableHelper;

/**
 * Servlet implementation class of Case list page
 */
@WebServlet(description = "Persons of Interest", urlPatterns = { "/PoI" })
public final class PoIServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private final DatastoreInterface dbInterface = new DatastoreInterface();

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

		table.addBeanColumn("Name", "name");
		table.addBeanColumn("Birthdate", "birthdate");

//		/*
//		 * Column 4: This is a special column. It adds a link to view the
//		 * Project. We need to pass the case identifier to the url.
//		 */
//		table.addLinkColumn(""	/* The header. We will leave it empty */,
//				"View Case" 	/* What should be displayed in every row */,
//				"Case?id=" 	/* This is the base url. The final url will be composed from the concatenation of this and the parameter below */, 
//				"caseId" 			/* For every case displayed, the ID will be retrieved and will be attached to the url base above */);
//        
		// Pass the table to the session. This will allow the respective jsp page to display the table.
		session.setAttribute("poI", table);

		table.addObjects(this.dbInterface.getAll(PoI.class));

		// Finally, proceed to the Projects.jsp page which will render the Projects
		this.getServletContext().getRequestDispatcher("/PoI.jsp").forward(request, response);
	}
}
