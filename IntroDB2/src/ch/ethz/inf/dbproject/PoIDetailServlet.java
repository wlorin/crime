package ch.ethz.inf.dbproject;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ch.ethz.inf.dbproject.model.DatastoreInterfaceSimpleDatabase;
import ch.ethz.inf.dbproject.model.PoI;
import ch.ethz.inf.dbproject.model.PoINote;
import ch.ethz.inf.dbproject.util.UserManagement;
import ch.ethz.inf.dbproject.util.html.BeanTableHelper;

/**
 * Servlet implementation class of Case list page
 */
@WebServlet(description = "Persons of Interest Detail", urlPatterns = { "/PoIDetail" })
public final class PoIDetailServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private final DatastoreInterfaceSimpleDatabase dbInterface = new DatastoreInterfaceSimpleDatabase();

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public PoIDetailServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected final void doGet(final HttpServletRequest request, final HttpServletResponse response) 
			throws ServletException, IOException {

		final HttpSession session = request.getSession(true);

		
		final String action = request.getParameter("action");
		final String idString = request.getParameter("PoIId");
		if (action == null && idString == null) {
			this.getServletContext().getRequestDispatcher("/Cases").forward(request, response);
			return;
		}

		try {

			if ("delete".equals(action)) {
				final String sId = request.getParameter("id");
				Integer noteId = Integer.valueOf(sId);
				int poiId = dbInterface.getPoIIdFromPoINote(noteId);
				dbInterface.deletePoINote(noteId);
				final String poiUrl = "PoIDetail?PoIId=" + Integer.toString(poiId);
				response.sendRedirect(poiUrl);
				return;
			}
			
			final Integer id = Integer.parseInt(idString);
			final PoI poi = this.dbInterface.getById(Long.valueOf(id), PoI.class);
			session.setAttribute("poiId", poi.getId());
			
			/*******************************************************
			 * Construct a table to present all properties of a case
			 *******************************************************/
			final BeanTableHelper<PoI> table = new BeanTableHelper<PoI>(
					"cases" 		/* The table html id property */,
					"casesTable" /* The table html class property */,
					PoI.class 	/* The class of the objects (rows) that will be displayed */
			);
			
			// Add columns to the new table

			/*
			 * Column 1: The name of the item (This will probably have to be changed)
			 */
			table.addBeanColumn("Name", "name");
			table.addBeanColumn("Birthdate", "birthdate");
			
			table.addLinkColumn("Convictions"	/* The header. We will leave it empty */,
					"View Convictions" 	/* What should be displayed in every row */,
					"Conviction?PoIId=" 	/* This is the base url. The final url will be composed from the concatenation of this and the parameter below */, 
					"id" 			/* For every case displayed, the ID will be retrieved and will be attached to the url base above */);
	  
			
			table.addObject(poi);
			table.setVertical(true);			

			session.setAttribute("poi", table);
			
			final BeanTableHelper<PoINote> poiNoteTable = new BeanTableHelper<PoINote>(
					"cases" 		/* The table html id property */,
					"casesTable" /* The table html class property */,
					PoINote.class 	/* The class of the objects (rows) that will be displayed */
			);
			
			poiNoteTable.addBeanColumn("Author", "username");
			poiNoteTable.addBeanColumn("Date", "timestamp");
			poiNoteTable.addBeanColumn("", "Note");
			
			if (UserManagement.isUserLoggedIn(session)) {
				 poiNoteTable.addLinkColumn("Delete Note", "Delete", "PoIDetail?action=delete&id=", "id");
			}
			
			final List<PoINote> notes = dbInterface.getPoINote(id);
			poiNoteTable.addObjects(notes);
			
			if (notes.size() == 0) {
				request.setAttribute("poiNotes", "<i>No notes</i>");
			}
			else {
				request.setAttribute("poiNotes", poiNoteTable);
			}
			this.getServletContext().getRequestDispatcher("/PoIDetail.jsp").forward(request, response);
		} catch (final Exception ex) {
			ex.printStackTrace();
			this.getServletContext().getRequestDispatcher("/PoI.jsp").forward(request, response);
		}
	}
}
