package ch.ethz.inf.dbproject.forms;

import static ch.ethz.inf.dbproject.util.Constant.WEB_ROOT;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;

import ch.ethz.inf.dbproject.forms.fields.Field;
import ch.ethz.inf.dbproject.forms.fields.HiddenField;
import ch.ethz.inf.dbproject.forms.fields.TextArea;
import ch.ethz.inf.dbproject.model.CaseNote;
import ch.ethz.inf.dbproject.model.DatastoreInterfaceMySQL;
import ch.ethz.inf.dbproject.util.UserManagement;


public class CaseNoteForm extends CommentForm<CaseNote> {

	final String comment = "Comment";
	final String caseId = CommentForm.REFERENCE_ID;

	
	TextArea fieldComment = new TextArea(comment);
	HiddenField fieldCase = new HiddenField(caseId, false);

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<Field> newFormFields() {
		return (List<Field>) (List) Arrays.asList(
				new Field[]{
				fieldComment,
				fieldCase
		});
	}
	
	@Override
	protected CaseNote processNewForm(List<Field> fields, HashMap<String, String> values, HttpSession session) {
		requireNotEmpty(comment, values);
		
		int caseId = Integer.valueOf(StringUtils.strip(fieldCase.parse(values)));
		 String comment = StringUtils.strip(fieldComment.getValue(values));
		
		return new DatastoreInterfaceMySQL().insertComment(comment, caseId, UserManagement.getCurrentlyLoggedInUser(session).getUserid());
	}

	@Override
	protected String getNewFormTitle() {
		return "Create new case note";
	}

	@Override
	protected void onNewSuccess(CaseNote result, ServletContext servletContext,
			HttpServletRequest request, HttpServletResponse response,
			HttpSession session) throws ServletException, IOException {
		response.sendRedirect(WEB_ROOT + "Case?id=" + result.getId());
	}

	@Override
	protected void onEditSuccess(CaseNote result,
			ServletContext servletContext, HttpServletRequest request,
			HttpServletResponse response, HttpSession session)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected CaseNote processEditForm(List<Field> fields,
			HashMap<String, String> values, HttpSession session) {
		// TODO Auto-generated method stub
		return null;
	}
}
