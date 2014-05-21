package ch.ethz.inf.dbproject;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ch.ethz.inf.dbproject.model.Case;
import ch.ethz.inf.dbproject.model.DatastoreInterfaceMySQL;
import ch.ethz.inf.dbproject.util.html.BeanTableHelper;

/**
 * Servlet implementation class Search
 */
@WebServlet(description = "The search page for cases", urlPatterns = { "/Search" })
public final class SearchServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private final DatastoreInterfaceMySQL dbInterface = new DatastoreInterfaceMySQL();
		
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SearchServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected final void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
		
		final HttpSession session = request.getSession(true);
		
		/*******************************************************
		 * Construct a table to present all our search results
		 *******************************************************/
		final BeanTableHelper<Case> table = new BeanTableHelper<Case>(
				"cases" 		/* The table html id property */,
		"casesTable" /* The table html class property */,
		Case.class 	/* The class of the objects (rows) that will bedisplayed */
		);
		
		// Add columns to the new table
		
		/*
		 * Column 1: The name of the item (This will probably have to be changed)
		 */
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
		 * Case. We need to pass the case identifier to the url.
		 */
		table.addLinkColumn(""	/* The header. We will leave it empty */,
		"View Case" 	/* What should be displayed in every row */,
		"Case?id=" 	/* This is the base url. The final url will be composed from the concatenation of this and the parameter below */, 
		"id" 		/* For every case displayed, the ID will be retrieved and will be attached to the url base above */);
		
		// Pass the table to the session. This will allow the respective jsp page to display the table.
		session.setAttribute("results", table);
		final String filter = request.getParameter("filter");
		
		if (filter != null) {
				
			if(filter.equals("description")) {
				
				final String name = request.getParameter("description");
				table.addObjects(this.dbInterface.searchByName(name));
			
			} else if (filter.equals("category")) {
			
				final String category = request.getParameter("category");
				table.addObjects(this.dbInterface.getProjectsByCategory(category));
			
			} else if (filter.equals("status")) {
			
				final String status = request.getParameter("status");
				table.addObjects(this.dbInterface.getByStatus(status));
			
			} else if (filter.equals("poiname")) {
			
				final String poiname = request.getParameter("poiname");
				table.addObjects(this.dbInterface.getInvolvedPoI(poiname));
			
			} else if (filter.equals("anotherattribute")) {
			
			// TODO implement this!		
			
			}		
		}
		

		// Finally, proceed to the Search.jsp page which will render the search results
        this.getServletContext().getRequestDispatcher("/Search.jsp").forward(request, response);	        
	}
}