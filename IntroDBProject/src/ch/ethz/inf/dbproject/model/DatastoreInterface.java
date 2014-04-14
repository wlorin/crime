package ch.ethz.inf.dbproject.model;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

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
	
	/**
	 * Will return null if users with given credentials does not exist.
	 * @param number
	 * @return
	 */
	public final User tryGetUserFromCredentials(String name, String password) {
		Class<User> userClass = User.class;
		String tableName = getTableName(userClass);
		try (
			PreparedStatement stmt = 
			this.sqlConnection.prepareStatement("SELECT * FROM" + tableName + "WHERE Name=? AND Password=MD5(?)");
		) {
			stmt.setString(1, name);
			stmt.setString(2, password);
			List<User> users = all(stmt, userClass);
			if (users.size() == 1) {
				return users.get(0);
			}
			return null;
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
	
	public Case insertCase(String name, String state, String location, Date date, Time time) {
		return insert(Case.class, 
				"Name", name, 
				"Status", state,
				"Location", location,
				"Date", date,
				"time", time
		);
	}
	
	public CaseNote insertComment(String comment, int caseId, int userid) {
		return insert(CaseNote.class, 
				"CaseId", caseId, 
				"Note", comment,
				"UserId", userid
		);
	}

	public PoI insertPoI(String name, Date birthdate) {
		return insert(PoI.class, 
				"Name", name, 
				"Birthdate", birthdate
		);
	}

	public PoINote insertPoINote(String comment, int poiId, int userid) {
		return insert(PoINote.class, 
				"PoIId", poiId, 
				"Note", comment,
				"UserId", userid
		);
	}

	public <T extends Entity> T insert(Class<T> clazz, Object... fields) {
		if (fields.length % 2 != 0) {
			throw new IllegalArgumentException("Pair fieldnames and field values");
		}
		String tableName = getTableName(clazz);
		
		ArrayList<String> columnNames = new ArrayList<>();
		for (int i_columnName = 0; i_columnName < fields.length; i_columnName += 2) {
			String columnName = (String) fields[i_columnName];
			columnNames.add(columnName);
		}
		
		ArrayList<String> placeholders = new ArrayList<>();
		for (int i_data = 1; i_data < fields.length; i_data += 2) {
			Object data = fields[i_data];			
			placeholders.add(data == null ? "NULL" : "?");
		}
		
		try (
				PreparedStatement stmt = this.sqlConnection.prepareStatement("INSERT INTO " + tableName +" (" + StringUtils.join(columnNames, ", ") +") VALUES (" + StringUtils.join(placeholders, ", ") + ")", Statement.RETURN_GENERATED_KEYS);
			) {
				int nextPlaceholder = 1;
				for (int i_data = 1; i_data < fields.length; i_data += 2) {
					Object data = fields[i_data];
					if (data instanceof String) {
						stmt.setString(nextPlaceholder++, (String) data);
					}
					else if (data instanceof Integer) {
						stmt.setInt(nextPlaceholder++, (Integer) data);
					}
					else if (data instanceof Long) {
						stmt.setLong(nextPlaceholder++, (Long) data);
					}
					else if (data instanceof Date) {
						stmt.setDate(nextPlaceholder++, (Date) data);
					}
					else if (data instanceof Time) {
						stmt.setTime(nextPlaceholder++, (Time) data);
					}
					else if (data == null) {
						// skip
					}
					else {
						throw new UnsupportedOperationException("Type not supported: " + data.getClass());
					}
				}
				
				return getById(insert(stmt), clazz);
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
			this.sqlConnection.prepareStatement("SELECT Case.CaseId, Case.CrimeId, Case.Location, Case.Date, Case.Time, Case.Status, Case.Name FROM" + 
			tableName + ", `Crime` WHERE Case.CrimeId = Crime.CrimeId and Crime.Crime = '" + category + "';");
		) {
			return all(stmt, Case.class);
		}  catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<Case> getProjectsWithoutCategory() {
		String tableName = getTableName(Case.class);
		try (
			PreparedStatement stmt = 
			this.sqlConnection.prepareStatement("SELECT Case.CaseId, Case.CrimeId, Case.Location, Case.Date, Case.Time, Case.Status, Case.Name FROM" + 
			tableName +  "WHERE Case.CrimeId is null;");
		) {
			return all(stmt, Case.class);
		}  catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<Case> searchByName(String string) {
		String tableName = getTableName(Case.class);
		try (
			PreparedStatement stmt = 
			this.sqlConnection.prepareStatement("SELECT * FROM " + tableName + " WHERE `Name` = '" + string + "';");
		) {
			return all(stmt, Case.class);
		}  catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}


	public List<CaseNote> getCaseNotesFrom(int caseId) {
		String tableName = getTableName(CaseNote.class);
		try (
			PreparedStatement stmt = this.sqlConnection.prepareStatement("SELECT * FROM" + tableName +  " WHERE caseId=? ORDER BY CaseNoteId DESC");
		) {
			stmt.setInt(1, caseId);
			return all(stmt, CaseNote.class);
			
		}  catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}


	
	public List<PoI> getAllSuspects(Integer id) {
		String tableName = getTableName(PoI.class);
		try (
			PreparedStatement stmt = 
			this.sqlConnection.prepareStatement("SELECT PoI.* FROM" + 
			tableName +  ", `Suspect` WHERE Suspect.CaseId = " + id + " and PoI.PoIId = Suspect.PoIId;");
		) {
			return all(stmt, PoI.class);

		}  catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}



public List<Conviction> getAllConvictions(Integer id) {
		String tableName = getTableName(Conviction.class);
		try (
			PreparedStatement stmt = 
			this.sqlConnection.prepareStatement("SELECT Convicted.Date, Convicted.Sentence, Convicted.CaseId FROM" + 
			tableName +  ", `PoI` WHERE PoI.PoIId = " + id + " and PoI.PoIId = Convicted.PoIId;");
		) {
			return all(stmt, Conviction.class);

		}  catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
}
