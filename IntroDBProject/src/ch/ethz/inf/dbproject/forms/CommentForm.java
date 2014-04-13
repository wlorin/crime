package ch.ethz.inf.dbproject.forms;

import java.util.Arrays;
import java.util.List;

import ch.ethz.inf.dbproject.forms.fields.Field;
import ch.ethz.inf.dbproject.forms.fields.HiddenField;
import ch.ethz.inf.dbproject.forms.fields.TextArea;
import ch.ethz.inf.dbproject.model.Note;


public abstract class CommentForm<T extends Note> extends Form<T> {

	final String comment = "Comment";
	final String refId = "ReferenceId";

	
	TextArea fieldComment = new TextArea(comment);
	HiddenField fieldReference = new HiddenField(refId, false);

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<Field> newFormFields() {
		return (List<Field>) (List) Arrays.asList(
				new Field[]{
				fieldComment,
				fieldReference
		});
	}

	@Override
	public List<Field> editFormFields() {
		throw new UnsupportedOperationException("Cannot edit Note");
	}

	@Override
	protected String getMethod() {
		return "POST";
	}

	@Override
	protected String getNewFormTitle() {
		return "Create new Note";
	}

	@Override
	protected String getEditFormTitle() {
		throw new UnsupportedOperationException("Cannot edit Note");
	}
}
