package ch.ethz.inf.dbproject;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ch.ethz.inf.dbproject.forms.CaseNoteForm;
import ch.ethz.inf.dbproject.forms.CommentForm;
import ch.ethz.inf.dbproject.forms.OpenCloseButton;
import ch.ethz.inf.dbproject.model.Case;
import ch.ethz.inf.dbproject.model.CaseNote;
import ch.ethz.inf.dbproject.model.DatastoreInterface;
import ch.ethz.inf.dbproject.util.UserManagement;
import ch.ethz.inf.dbproject.util.html.BeanTableHelper;

/**
 * Servlet implementation class of Case Details Page
 */
@WebServlet(description = "Displays a specific case.", urlPatterns = { "/Case" })
public final class CaseServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private final DatastoreInterface dbInterface = new DatastoreInterface();

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public CaseServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected final void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {

		final HttpSession session = request.getSession(true);

		final String idString = request.getParameter("id");
		if (idString == null) {
			this.getServletContext().getRequestDispatcher("/Cases").forward(request, response);
			return;
		}

		try {

			final Integer id = Integer.parseInt(idString);
			
			final Case aCase = this.dbInterface.getById(Long.valueOf(id), Case.class);

			
			/*******************************************************
			 * Construct a table to present all properties of a case
			 *******************************************************/
			final BeanTableHelper<Case> table = new BeanTableHelper<Case>(
					"cases" 		/* The table html id property */,
					"casesTable" /* The table html class property */,
					Case.class 	/* The class of the objects (rows) that will be displayed */
			);

			// Add columns to the new table

			/*
			 * Column 1: The name of the item (This will probably have to be changed)
			 */
			table.addBeanColumn("Case Description", "crime");
			table.addBeanColumn("Case Name", "name");

			/*
			 * Columns 2 & 3: Some random fields. These should be replaced by i.e. funding progress, or time remaining
			 */
			table.addBeanColumn("Status", "status");
			table.addBeanColumn("Location", "location");
			table.addBeanColumn("Date", "date");
			table.addBeanColumn("Time", "time");
			table.addLinkColumn("Suspects"	/* The header. We will leave it empty */,
					"View Suspects" 	/* What should be displayed in every row */,
					"Suspect?CaseId=" 	/* This is the base url. The final url will be composed from the concatenation of this and the parameter below */, 
					"id" 			/* For every case displayed, the ID will be retrieved and will be attached to the url base above */);

			table.addObject(aCase);
			table.setVertical(true);			

			session.setAttribute("caseTable", table);		
			
			
			final BeanTableHelper<CaseNote> caseNotes = new BeanTableHelper<CaseNote>(
					"cases" 		/* The table html id property */,
					"casesTable" /* The table html class property */,
					CaseNote.class 	/* The class of the objects (rows) that will be displayed */
			);

			caseNotes.addBeanColumn("Author", "username");
			caseNotes.addBeanColumn("Date", "timestamp");
			caseNotes.addBeanColumn("Note", "note");

			final List<CaseNote> notes = aCase.getCaseNotes();
			caseNotes.addObjects(notes);
			
			session.setAttribute("caseTable", table);
			session.setAttribute("caseNoteTable", (notes.size() == 0) ? "<i>No notes</i>" : caseNotes);

			session.setAttribute("entity", aCase);
			session.setAttribute("caseId", aCase.getId());
			
			
			if (UserManagement.isUserLoggedIn(session)) {
				if (aCase.isOpen()) {
					request.setAttribute("newCaseNote", new CaseNoteForm().generateNewFormWith(CommentForm.REFERENCE_ID, "" + aCase.getId()));
					if (dbInterface.getAllSuspects(id).size() > 0) {
						request.setAttribute("openCloseButton", "Cannot close case because there are still Suspects assigned to it");
					}
					else {
						request.setAttribute("openCloseButton", 
								new OpenCloseButton()
								.generateForm("Close Case", OpenCloseButton.ACTION_CLOSE, "" + aCase.getId())
						);
					}
				}
				else {
					request.setAttribute("newCaseNote", "");
					request.setAttribute("openCloseButton", 
							new OpenCloseButton()
							.generateForm("Reopen Case", OpenCloseButton.ACTION_OPEN, "" + aCase.getId())
					);
				}
			}
			
			this.getServletContext().getRequestDispatcher("/Case.jsp").forward(request, response);
		} catch (final Exception ex) {
			ex.printStackTrace();
			this.getServletContext().getRequestDispatcher("/Cases.jsp").forward(request, response);
		}
	}
}