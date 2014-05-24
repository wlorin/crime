package ch.ethz.inf.dbproject;


import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ch.ethz.inf.dbproject.model.Conviction;
import ch.ethz.inf.dbproject.model.DatastoreInterfaceSimpleDatabase;
import ch.ethz.inf.dbproject.util.html.BeanTableHelper;

/**
 * Servlet implementation class of Convictions
 */
@WebServlet(description = "Displays convictions.", urlPatterns = { "/Conviction" })
public final class ConvictionServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private final DatastoreInterfaceSimpleDatabase dbInterface = new DatastoreInterfaceSimpleDatabase();

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ConvictionServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected final void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {

		final HttpSession session = request.getSession(true);

		final String idString = request.getParameter("PoIId");
		if (idString == null) {
			this.getServletContext().getRequestDispatcher("/PoI").forward(request, response);
			return;
		}

		try {

			final Integer id = Integer.parseInt(idString);

			session.setAttribute("poiname", this.dbInterface.getPoiNameById(id));
			
			final BeanTableHelper<Conviction> table = new BeanTableHelper<Conviction>(
					"conviction" 		/* The table html id property */,
					"casesTable" 		/* The table html class property */,
					Conviction.class 	/* The class of the objects (rows) that will be displayed */
			);

			table.addBeanColumn("Case", "casename");
			table.addBeanColumn("Crime", "crime");
			table.addBeanColumn("Sentence", "sentence");
			table.addBeanColumn("Date of Conviction", "date");
			
			session.setAttribute("conviction", table);		
			
			table.addObjects(this.dbInterface.getAllConvictions(id));
			
			this.getServletContext().getRequestDispatcher("/Conviction.jsp").forward(request, response);
		} catch (final Exception ex) {
			ex.printStackTrace();
			this.getServletContext().getRequestDispatcher("/PoI.jsp").forward(request, response);
		}
	}
}

