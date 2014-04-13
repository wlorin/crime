package ch.ethz.inf.dbproject;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ch.ethz.inf.dbproject.forms.CreationForm;

/**
 * Servlet implementation class PostHandlerServlet
 */
@WebServlet("/PostHandlerServlet")
public class PostHandlerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PostHandlerServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			@SuppressWarnings("unchecked")
			Class<? extends CreationForm<?>> formHandlerClass = (Class<? extends CreationForm<?>>) Class.forName(request.getParameter(CreationForm.FIELD_TARGET_CLASS));
			String newOrEdit = request.getParameter(CreationForm.FIELD_NEW_OR_EDIT);
			if ("new".equals(newOrEdit)) {
				CreationForm<?> formHandler = formHandlerClass.newInstance();
				formHandler.processNewForm(this.getServletContext(), request, response, request.getSession());
			}
			else if ("edit".equals(newOrEdit)) {
				
			}
			else {
				
			}
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO Auto-generated method stub
	}

}
