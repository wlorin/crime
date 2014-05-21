package ch.ethz.inf.dbproject;

import static ch.ethz.inf.dbproject.util.Constant.WEB_ROOT;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ch.ethz.inf.dbproject.model.DatastoreInterfaceMySQL;
import ch.ethz.inf.dbproject.model.User;
import ch.ethz.inf.dbproject.util.UserManagement;

@WebServlet(description = "Page that processes the logout", urlPatterns = { "/Logout" })
public final class LogoutServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private final DatastoreInterfaceMySQL dbInterface = new DatastoreInterfaceMySQL();

	public final static String SESSION_USER_LOGGED_IN = "userLoggedIn";
	public final static String SESSION_USER_DETAILS = "userDetails";

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public LogoutServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected final void doGet(final HttpServletRequest request,
			final HttpServletResponse response) throws ServletException,
			IOException {

		final HttpSession session = request.getSession(true);
		final User loggedUser = UserManagement.getCurrentlyLoggedInUser(session);

		if (loggedUser == null) {
			// Not logged in!

		} else {
			// Logged in
			session.removeAttribute(UserManagement.SESSION_USER);
			session.setAttribute(UserServlet.SESSION_USER_LOGGED_IN, false);
		}

		
		// Finally, proceed to the User.jsp page which will renden the profile
		response.sendRedirect(WEB_ROOT);

	}

}
