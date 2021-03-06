package ch.ethz.inf.dbproject;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ch.ethz.inf.dbproject.forms.CrimeForm;
import ch.ethz.inf.dbproject.forms.PersonOfInterestForm;
import ch.ethz.inf.dbproject.model.DatastoreInterfaceSimpleDatabase;
import ch.ethz.inf.dbproject.model.User;
import ch.ethz.inf.dbproject.util.Constant;
import ch.ethz.inf.dbproject.util.UserManagement;
import ch.ethz.inf.dbproject.util.html.BeanTableHelper;

@WebServlet(description = "Page that displays the user login / logout options.", urlPatterns = { "/User" })
public final class UserServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private final DatastoreInterfaceSimpleDatabase dbInterface = new DatastoreInterfaceSimpleDatabase();

	public final static String SESSION_USER_LOGGED_IN = "userLoggedIn";
	public final static String SESSION_USER_DETAILS = "userDetails";
	public final static String SESSION_USER_CREDENTIALS_INCORRECT = "userCredentialsIncorrect";

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UserServlet() {
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
			session.setAttribute(SESSION_USER_LOGGED_IN, false);
		} else {
			// Logged in
			final BeanTableHelper<User> userDetails = new BeanTableHelper<User>("userDetails", "userDetails", User.class);
			userDetails.addBeanColumn("Name", "name");
			userDetails.addObject(loggedUser);

			session.setAttribute(SESSION_USER_LOGGED_IN, true);
			session.setAttribute(SESSION_USER_DETAILS, userDetails);
			
			request.setAttribute("formNewPoI", new PersonOfInterestForm().generateNewForm());
			request.setAttribute("formNewCrime", new CrimeForm().generateNewForm());
		}

		// TODO display registration

		final String action = request.getParameter("action");
		if (action != null && action.trim().equals("login") 	&& loggedUser == null) {

			final String username = request.getParameter("username");
			// Note: It is really not safe to use HTML get method to send passwords.
			// However for this project, security is not a requirement.
			final String password = request.getParameter("password");

			User user = dbInterface.tryGetUserFromCredentials(username, password);
			if (user != null) {
				session.setAttribute(UserManagement.SESSION_USER, user);
				response.sendRedirect(Constant.WEB_ROOT + "User");
				return;
			}
			else {
			
			}
		}

		// Finally, proceed to the User.jsp page which will renden the profile
		this.getServletContext().getRequestDispatcher("/User.jsp").forward(request, response);

	}

}
