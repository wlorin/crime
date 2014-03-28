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
	private Connection sqlConnection;

	public DatastoreInterface() {
		this.sqlConnection = MySQLConnection.getInstance().getConnection();
	}
	
	public final Case getCaseById(final int id) {
		return null;
	}
	
	public final <T extends Entity> List<T> getAll(Class<T> clazz) {
		String tableName = getTableName(clazz);
		
		try (
			PreparedStatement stmt = this.sqlConnection.prepareStatement("SELECT * FROM " + tableName);
		) {
			return all(stmt, clazz);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}			
	}

	private <T extends Entity> List<T> all(PreparedStatement stmt, Class<T> clazz) {
		try (
			ResultSet rs = stmt.executeQuery();
		) {
			Constructor<T> constructor = clazz.getConstructor(ResultSet.class);
			List<T> ts = new ArrayList<T>(); 
			while (rs.next()) {
				ts.add(constructor.newInstance(rs));
			}
			
			return ts;
			
		} catch (SQLException 
				| InstantiationException 
				| IllegalAccessException 
				| IllegalArgumentException 
				| InvocationTargetException 
				| NoSuchMethodException 
				| SecurityException e
		) {
			e.printStackTrace();
			return null;
		}
	}

	private <T extends Entity> String getTableName(Class<T> clazz) {
		TableName tableAnnotation = clazz.getAnnotation(TableName.class);
		if (tableAnnotation != null) {
			return tableAnnotation.name();
		}
		else {
			return "`" + clazz.getSimpleName() + "`";
		}
	}
}
