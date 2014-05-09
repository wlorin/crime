package ch.ethz.inf.dbproject.model;

import java.util.ArrayList;
import java.util.List;

import ch.ethz.inf.dbproject.model.simpleDatabase.Tuple;
import ch.ethz.inf.dbproject.model.simpleDatabase.operators.Scan;
import ch.ethz.inf.dbproject.model.simpleDatabase.operators.Select;


public final class DatastoreInterfaceSimpleDatabase implements DatastoreInterface {

	public DatastoreInterfaceSimpleDatabase() {
	}
	
	/* (non-Javadoc)
	 * @see ch.ethz.inf.dbproject.model.DatastoreInterface#getCaseById(int)
	 */
	@Override
	public final Case getCaseById(final int id) {
	
		/**
		 * TODO this method should return the case with the given id
		 */
		final Scan scan = new Scan("cases.txt", 
			new String[] {
				"id",
				"name",
				"field2",
				"field3"
			}
		);
		
		final Select<Integer> select = new Select<Integer>(scan, "id", id);
		
		if (select.moveNext()) {
			
			final Tuple tuple = select.current();
			final Case p = new Case(
				tuple.getInt(0), 
				tuple.get(1),
				tuple.get(2),
				tuple.getInt(3)
			);
			return p;
			
		} else {
			
			return null;
			
		}		
	}

	@Override
	public final List<Case> getAllCases() {
		// TODO
		return null;
		
	}

	@Override
	public List<Case> getCaseByName(String name) {
		// TODO 
		return null;
	}

	@Override
	public List<Case> getCasesByCategory(Category category) {
		// TODO 
		return null;
	}

	@Override
	public List<Case> getCasesByCity(String city) {
		// TODO 
		return null;
	}

	@Override
	public List<Case> getMostRecentCases() {
		// TODO 
		return null;
	}

	@Override
	public List<Case> getOldestUnsolvedCases() {
		// TODO 
		return null;
	}

	@Override
	public List<Comment> getCaseComments(int projectId) {
		// TODO 
		return null;
	}
	
	@Override
	public final List<Category> getAllCategories() {

		/**
		 * TODO This method should return all the different categories in the
		 * database.
		 * 
		 * For the time being we return some random values.
		 */
		final List<Category> categories = new ArrayList<Category>();
		categories.add(new Category("Theft"));
		categories.add(new Category("Fraud"));
		return categories;
	}

	@Override
	public User getUser(String username, String password) {
		// TODO
		return null;
	}

	@Override
	public List<Case> getCaseByOwner(int userId) {
		// TODO 
		return null;
	}
}
