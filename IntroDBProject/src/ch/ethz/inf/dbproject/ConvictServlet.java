package ch.ethz.inf.dbproject;


import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ch.ethz.inf.dbproject.model.Conviction;
import ch.ethz.inf.dbproject.model.DatastoreInterface;
import ch.ethz.inf.dbproject.util.html.BeanTableHelper;

/**
 * Servlet implementation class of Convictions
 */
@WebServlet(description = "Displays convictions.", urlPatterns = { "/Convict" })
public final class ConvictServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private final DatastoreInterface dbInterface = new DatastoreInterface();

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ConvictServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected final void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {

		final HttpSession session = request.getSession(true);
		
		final String CaseIdString = request.getParameter("CaseId");
		final String PoIIdString = request.getParameter("PoIId");
		if (CaseIdString == null || PoIIdString == null) {
			this.getServletContext().getRequestDispatcher("/Cases").forward(request, response);
			return;
		}
	
		this.getServletContext().getRequestDispatcher("/Convict.jsp").forward(request, response);
	}
}

