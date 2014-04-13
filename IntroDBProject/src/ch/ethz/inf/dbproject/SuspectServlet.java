package ch.ethz.inf.dbproject;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ch.ethz.inf.dbproject.model.PoI;
import ch.ethz.inf.dbproject.model.DatastoreInterface;
import ch.ethz.inf.dbproject.util.html.BeanTableHelper;

/**
 * Servlet implementation class of Suspects
 */
@WebServlet(description = "Displays suspects for a case.", urlPatterns = { "/Suspect" })
public final class SuspectServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private final DatastoreInterface dbInterface = new DatastoreInterface();

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public SuspectServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected final void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {

		final HttpSession session = request.getSession(true);

		final String idString = request.getParameter("id");
		if (idString == null) {
			System.out.println("No ID");
			this.getServletContext().getRequestDispatcher("/Cases").forward(request, response);
			return;
		}

		try {

			final Integer id = Integer.parseInt(idString);

			final BeanTableHelper<PoI> table = new BeanTableHelper<PoI>(
					"suspect" 		/* The table html id property */,
					"casesTable" /* The table html class property */,
					PoI.class 	/* The class of the objects (rows) that will be displayed */
			);

			table.addBeanColumn("Name", "name");
			table.addBeanColumn("Birthdate", "birthdate");		

			session.setAttribute("suspect", table);		
			
			table.addObjects(this.dbInterface.getAllSuspects(id));
			
			this.getServletContext().getRequestDispatcher("/Suspect.jsp").forward(request, response);
		} catch (final Exception ex) {
			ex.printStackTrace();
			this.getServletContext().getRequestDispatcher("/Cases.jsp").forward(request, response);
		}
	}
}
