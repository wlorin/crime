package ch.ethz.inf.dbproject.forms.fields;

import java.util.ArrayList;
import java.util.List;

import ch.ethz.inf.dbproject.model.Crime;
import ch.ethz.inf.dbproject.model.DatastoreInterfaceSimpleDatabase;


public class CrimeField extends SelectField {

	public CrimeField(String displayName) {
		super(displayName, crimeItems());
	}

	private static String[] crimeItems() {
		List<String> value__name = new ArrayList<String>();
		List<Crime> all = new DatastoreInterfaceSimpleDatabase().getAll(Crime.class);
		for (Crime category : all) {
			value__name.add("" + category.getId());
			value__name.add(category.getName());
		}
		
		return value__name.toArray(new String[value__name.size()]);
	}
}
