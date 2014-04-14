package ch.ethz.inf.dbproject.forms;

import static ch.ethz.inf.dbproject.util.Constant.WEB_ROOT;

import java.io.IOException;
import java.sql.Date;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;

import ch.ethz.inf.dbproject.forms.fields.CrimeField;
import ch.ethz.inf.dbproject.forms.fields.DateField;
import ch.ethz.inf.dbproject.forms.fields.Field;
import ch.ethz.inf.dbproject.forms.fields.HiddenField;
import ch.ethz.inf.dbproject.forms.fields.StringField;
import ch.ethz.inf.dbproject.model.Conviction;
import ch.ethz.inf.dbproject.model.DatastoreInterface;

public class ConvictForm extends CreationForm<Conviction> {
	final public static String REFERENCE_CASEID = "CaseId";
	final public static String REFERENCE_POIID = "PoIId";
	
	final String sentence = "Sentence";
	final String date = "ConvictionDate";
	final String crime = "Crime";
	final DateField fieldConviction = new DateField(date, false);
	final private CrimeField categoryField = new CrimeField(crime);
	HiddenField fieldCaseId = new HiddenField(REFERENCE_CASEID, false);
	HiddenField fieldPoIId = new HiddenField(REFERENCE_POIID, false);
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	protected List<Field> newFormFields() {
		return (List<Field>) (List) Arrays.asList(new Field[]{
				new StringField(sentence, false),
				fieldConviction,
				categoryField, 
				fieldCaseId,
				fieldPoIId
		});
	}

	@Override
	protected String getNewFormTitle() {
		/*DatastoreInterface dbInterface = new DatastoreInterface();
		String caseName = dbInterface.getById(caseId, Case.class).getName();
		String poiName = dbInterface.getById(poiId, PoI.class).getName();
		return "Convict " + poiName + " for Case " + caseName;*/
		return "Convict";
	}

	@Override
	protected String getEditFormTitle() {
		throw new UnsupportedOperationException("Cannot edit Conviction");
	}

	@Override
	protected void onNewSuccess(Conviction result,
			ServletContext servletContext, HttpServletRequest request,
			HttpServletResponse response, HttpSession session)
			throws ServletException, IOException {
		response.sendRedirect(WEB_ROOT + "Suspect?CaseId=" + result.getCaseId());
		
	}

	@Override
	protected Conviction processNewForm(List<Field> fields,
			HashMap<String, String> values, HttpSession session) {
		for (Entry<String, String> item : values.entrySet()) {
			System.out.println(item.getKey() + " => " + item.getValue());
		}
		System.out.println("processing form");
		requireNotEmpty(this.sentence, values);
		String sentence = StringUtils.strip(values.get(this.sentence));
		Date convictionDate = fieldConviction.parse(values);
		int crimeId = Integer.valueOf(values.get(this.crime));
		int caseId = Integer.valueOf(values.get(REFERENCE_CASEID));
		int poiId = Integer.valueOf(values.get(REFERENCE_POIID));
		return new DatastoreInterface().insertConviction(caseId, poiId, convictionDate, sentence, crimeId);
	}

	@Override
	protected String getMethod() {
		return "POST";
	}

}
