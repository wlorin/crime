package ch.ethz.inf.dbproject;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ch.ethz.inf.dbproject.model.Case;
import ch.ethz.inf.dbproject.model.Convict;
import ch.ethz.inf.dbproject.model.DatastoreInterfaceSimpleDatabase;
import ch.ethz.inf.dbproject.model.PoI;
import ch.ethz.inf.dbproject.util.UserManagement;
import ch.ethz.inf.dbproject.util.html.BeanTableHelper;

/**
 * Servlet implementation class of Suspects
 */
@WebServlet(description = "Displays suspects for a case.", urlPatterns = { "/Suspect" })
public final class SuspectServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private final DatastoreInterfaceSimpleDatabase dbInterface = new DatastoreInterfaceSimpleDatabase();

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

		final String idString = request.getParameter("CaseId");
		if (idString == null) {
			this.getServletContext().getRequestDispatcher("/Cases").forward(request, response);
			return;
		}

		try {
			final Integer caseId = Integer.parseInt(idString);
			if (UserManagement.isUserLoggedIn(session)) {
				String action = request.getParameter("action");
				if ("link".equals(action)) {
					String poiIdString = request.getParameter("PoIId");
					if (poiIdString != null) {
						int poiId = Integer.parseInt(poiIdString);
						dbInterface.insertSuspect(caseId, poiId);
					}
				}
				else if ("unlink".equals(action)) {
					String poiIdString = request.getParameter("PoIId");
					if (poiIdString != null) {
						int poiId = Integer.parseInt(poiIdString);
						dbInterface.removeSuspect(caseId, poiId);
					}
				}
				else if ("delete".equals(action)) {
					String poiCrime = request.getParameter("poi-crime");
					int dash = poiCrime.indexOf('-');
					String poiString = poiCrime.substring(0, dash);
					String crimeString = poiCrime.substring(dash+1);
					int poi = Integer.parseInt(poiString);
					int crime = Integer.parseInt(crimeString);
					dbInterface.deleteConviction(caseId, poi, crime);
				}
			}
			
			final BeanTableHelper<Convict> convicts = new BeanTableHelper<Convict>(
					"convicted", "casesTable", Convict.class);
			convicts.addBeanColumn("Name", "name");
			convicts.addBeanColumn("Birthdate", "birthdate");
			convicts.addBeanColumn("Crime", "crime");
			convicts.addBeanColumn("Conviction Date", "convictionDate");
			convicts.addBeanColumn("Sentence", "sentence");

			

			final BeanTableHelper<PoI> suspects = new BeanTableHelper<PoI>(
					"suspect" 		/* The table html id property */,
					"casesTable" /* The table html class property */,
					PoI.class 	/* The class of the objects (rows) that will be displayed */
			);

			suspects.addBeanColumn("Name", "name");
			suspects.addBeanColumn("Birthdate", "birthdate");
			final BeanTableHelper<PoI> pois = new BeanTableHelper<PoI>(
					"PoI",
					"casesTable",
					PoI.class
			);
			pois.addBeanColumn("Name", "name");
			pois.addBeanColumn("Birthdate", "birthdate");
			session.setAttribute("poisnotlinkted", "");
			session.setAttribute("suspect", suspects);
			final Case aCase = dbInterface.getById(caseId, Case.class);
			if (UserManagement.isUserLoggedIn(session) && aCase.isOpen()) {
				suspects.addLinkColumn("Unlink", "Unlink", "Suspect?CaseId=" + caseId + "&action=unlink&PoIId=", "id");
				suspects.addLinkColumn("Convict", "Convict", "Convict?CaseId=" + caseId + "&PoIId=", "id");
				pois.addLinkColumn("Link with Case", "Link", "Suspect?CaseId=" + caseId + "&action=link&PoIId=", "id");
				convicts.addLinkColumn("Delete Conviction", "Delete", "Suspect?action=delete&CaseId=" + caseId + "&poi-crime=", "poiCrime");
				session.setAttribute("poisnotlinkted", pois);
			}
			convicts.addObjects(dbInterface.getAllConvicts(caseId));
			suspects.addObjects(this.dbInterface.getAllSuspects(caseId));
			pois.addObjects(dbInterface.getAllPoIsNotLinkedToCase(caseId));
			
			session.setAttribute("convicts", convicts);	
			
			
			session.setAttribute("casename", aCase.getName());
			session.setAttribute("caseclosed", (aCase.isOpen() ? "false" : "true"));
			
			this.getServletContext().getRequestDispatcher("/Suspect.jsp").forward(request, response);
		} catch (final Exception ex) {
			ex.printStackTrace();
			this.getServletContext().getRequestDispatcher("/Cases.jsp").forward(request, response);
		}
	}
}
