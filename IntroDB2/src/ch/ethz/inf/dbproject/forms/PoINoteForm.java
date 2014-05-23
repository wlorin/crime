package ch.ethz.inf.dbproject.forms;

import static ch.ethz.inf.dbproject.util.Constant.WEB_ROOT;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;

import ch.ethz.inf.dbproject.forms.fields.Field;
import ch.ethz.inf.dbproject.model.DatastoreInterfaceSimpleDatabase;
import ch.ethz.inf.dbproject.model.PoINote;
import ch.ethz.inf.dbproject.util.UserManagement;


public class PoINoteForm extends CommentForm<PoINote> {

	final String comment = "Comment";
	final String caseId = CommentForm.REFERENCE_ID;

	@Override
	protected PoINote processNewForm(List<Field> fields, HashMap<String, String> values, HttpSession session) {
		requireNotEmpty(comment, values);
		
		int caseId = Integer.valueOf(StringUtils.strip(fieldReference.parse(values)));
		 String comment = StringUtils.strip(fieldComment.getValue(values));
		
		return new DatastoreInterfaceSimpleDatabase().insertPoINote(comment, caseId, UserManagement.getCurrentlyLoggedInUser(session).getUserid());
	}

	@Override
	protected void onNewSuccess(PoINote result, ServletContext servletContext,
			HttpServletRequest request, HttpServletResponse response,
			HttpSession session) throws ServletException, IOException {
		response.sendRedirect(WEB_ROOT + "PoIDetail?id=" + result.getCaseId());
	}

	@Override
	protected void onEditSuccess(PoINote result, ServletContext servletContext,
			HttpServletRequest request, HttpServletResponse response,
			HttpSession session) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected PoINote processEditForm(List<Field> fields,
			HashMap<String, String> values, HttpSession session) {
		// TODO Auto-generated method stub
		return null;
	}
}
