package ch.ethz.inf.dbproject.model;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
	
	public final <T extends Entity> T getById(final long id, Class<T> clazz) {
		String idColName = getIdColName(clazz);
		String tableName = getTableName(clazz);
		
		String sql = String.format("SELECT * FROM %s WHERE %s=?", tableName, idColName);
		try (
			PreparedStatement stmt = this.sqlConnection.prepareStatement(sql);
		) {
			stmt.setLong(1, id);
			List<T> all = all(stmt, clazz);
			
			if (all.size() == 1) {
				return all.get(0);
			}
			
			if (all.size() > 1) {
				throw new IllegalStateException("Expected at most one result from query: " + sql);
			}
			
			return null;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
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
			return "`" + getRawTableName(clazz) + "`";
	}
	
	private <T extends Entity> String getIdColName(Class<T> clazz) {
		return "`" + getRawTableName(clazz) + "Id`";
	}

	private <T extends Entity> String getRawTableName(Class<T> clazz) {
		TableName tableAnnotation = clazz.getAnnotation(TableName.class);
		if (tableAnnotation != null) {
			return tableAnnotation.name();
		}
		else {
			return clazz.getSimpleName();
		}
	}

	public final List<Case> getByStatus(String status) {
		String tableName = getTableName(Case.class);
		try (
			PreparedStatement stmt = this.sqlConnection.prepareStatement("SELECT * FROM" + tableName + "WHERE status = '" + status  + "';");
		) {
			return all(stmt, Case.class);
		}  catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public final List<Case> getMostRecentCases(int number) {
		String tableName = getTableName(Case.class);
		try (
			PreparedStatement stmt = 
			this.sqlConnection.prepareStatement("SELECT * FROM" + tableName + "WHERE status = 'open' ORDER BY date DESC LIMIT 0," + number + ";");
		) {
			return all(stmt, Case.class);
		}  catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public final List<Case> getOldestUnsolvedCases(int number) {
		String tableName = getTableName(Case.class);
		try (
			PreparedStatement stmt = 
			this.sqlConnection.prepareStatement("SELECT * FROM" + tableName + "WHERE status = 'open' ORDER BY date ASC LIMIT 0," + number + ";");
		) {
			return all(stmt, Case.class);
		}  catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public long insert(PreparedStatement statement) throws SQLException {
		statement.executeUpdate();
		try (
			ResultSet generatedKeys = statement.getGeneratedKeys()
		) {
			generatedKeys.next();
			return generatedKeys.getLong(1);
		}
	}

	public User insertUser(String name, String password) {
		try (
			PreparedStatement stmt = this.sqlConnection.prepareStatement("INSERT INTO `User` (Name, Password) VALUES (?, MD5(?))", Statement.RETURN_GENERATED_KEYS);
		) {
			stmt.setString(1, name);
			stmt.setString(2, password);
			return getById(insert(stmt), User.class);
		}  catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public String getCrimeById(int id) {
		try (
				PreparedStatement stmt = this.sqlConnection.prepareStatement("SELECT `Crime` FROM `Crime` WHERE `CrimeId` = " + id);
			) {
			    ResultSet rs = stmt.executeQuery();
				rs.next();
			    return rs.getString(1);
			}  catch (SQLException e) {
				e.printStackTrace();
				return null;
			}
		}

	public List<Case> getProjectsByCategory(String category) {
		String tableName = getTableName(Case.class);
		try (
			PreparedStatement stmt = 
			this.sqlConnection.prepareStatement("SELECT Case.CaseId, Case.CrimeId, Case.Location, Case.Date, Case.Time, Case.Status FROM" + 
			tableName + ", `Crime` WHERE Case.CrimeId = Crime.CrimeId and Crime.Crime = '" + category + "';");
		) {
			return all(stmt, Case.class);
		}  catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
}
