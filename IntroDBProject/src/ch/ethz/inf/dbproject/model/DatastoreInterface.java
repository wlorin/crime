package ch.ethz.inf.dbproject.model;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ch.ethz.inf.dbproject.database.MySQLConnection;
import ch.ethz.inf.dbproject.model.meta.Entity;
import ch.ethz.inf.dbproject.model.meta.TableName;

/**
 * This class should be the interface between the web application
 * and the database. Keeping all the data-access methods here
 * will be very helpful for part 2 of the project.
 */
public final class DatastoreInterface {

	//FIXME This is a temporary list of cases that will be displayed until all the methods have been properly implemented
	private final static Case[] staticCases = new Case[] { 
			new Case(0, "Noise pollution..", "1287.9%", 10000), 
			new Case(1, "Highway overspeed...", "54.7%", 250000),
			new Case(2, "Money Laundring...", "1.2%", 1000000),
			new Case(3, "Corruption...", "0.0%", 1000000000),
		};
	private final static List<Case> staticCaseList = new ArrayList<Case>();
	static {
		for (int i = 0; i < staticCases.length; i++) {
			staticCaseList.add(staticCases[i]);
		}
	}
	
	@SuppressWarnings("unused")
	private Connection sqlConnection;

	public DatastoreInterface() {
		this.sqlConnection = MySQLConnection.getInstance().getConnection();
	}
	
	public final Case getCaseById(final int id) {
	
		/**
		 * TODO this method should return the case with the given id
		 */
		
		if (id < staticCases.length) {
			return staticCases[id];
		} else {
			return null;
		}
		
	}
	
	public final <T extends Entity> List<T> getAll(Class<T> clazz) {
		String tableName;
		TableName tableAnnotation = clazz.getAnnotation(TableName.class);
		if (tableAnnotation != null) {
			tableName = tableAnnotation.name();
		}
		else {
			tableName =  clazz.getSimpleName();
		}
		
		try (
			PreparedStatement stmt = this.sqlConnection.prepareStatement("SELECT * FROM " + tableName);
			ResultSet rs = stmt.executeQuery();
		) {
			Constructor<T> constructor = clazz.getConstructor(ResultSet.class);
			List<T> cases = new ArrayList<T>(); 
			while (rs.next()) {
				cases.add(constructor.newInstance(rs));
			}
			
			return cases;
			
		} catch (SQLException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException ex) {			
			ex.printStackTrace();
			return null;
		}
	}
}
