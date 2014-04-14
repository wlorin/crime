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
	static private enum AUTOEXECUTE_MODE { INSERT, UPDATE };

	public DatastoreInterface() {
		this.sqlConnection = MySQLConnection.getInstance().getConnection();
	}
	
	public final <T extends Entity> T getById(final Long id, Class<T> clazz) {
		if (id == null) {
			return null;
		}
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
			return tableAnnotation.value();
		}
		else {
			return clazz.getSimpleName();
		}
	}
	public String getCasenameById(int id) {
		 try (
		 		PreparedStatement stmt = this.sqlConnection.prepareStatement("SELECT `Name` FROM `Case` WHERE `CaseId` = " + id);
		 ) {
			 ResultSet rs = stmt.executeQuery();
			 rs.next();
			 return rs.getString(1);
		 }  catch (SQLException e) {
			 e.printStackTrace();
			 return null;
		 }
	}
	
	public boolean isCaseClosed(int caseId) {
		return !getById(Long.valueOf(caseId), Case.class).isOpen();
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
	
	public Long execute(PreparedStatement statement) throws SQLException {
		statement.executeUpdate();
		try (
			ResultSet generatedKeys = statement.getGeneratedKeys()
		) {
			if (generatedKeys.next()) {
				return generatedKeys.getLong(1);
			}
			return null;
		}
	}
	
	public void removeSuspect(int caseId, int poiId) {
		runQuery("DELETE FROM Suspect WHERE CaseId=" + caseId + " AND PoIId=" + poiId);
	}
	public void deleteConviction(int caseId, int poiId, int crimeid) {
		runQuery("DELETE FROM Convicted WHERE CaseId=" + caseId + " AND PoIId=" + poiId + " AND CrimeId=" + crimeid);
	}

	public User insertUser(String name, String password) {
		try (
			PreparedStatement stmt = this.sqlConnection.prepareStatement("INSERT INTO `User` (Name, Password) VALUES (?, MD5(?))", Statement.RETURN_GENERATED_KEYS);
		) {
			stmt.setString(1, name);
			stmt.setString(2, password);
			return getById(execute(stmt), User.class);
		}  catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public final Suspect insertSuspect(int caseId, int poiId) {
		return autoExecute(AUTOEXECUTE_MODE.INSERT, Suspect.class, "CaseId", caseId, "PoIId", poiId);
	}
	
	public Case insertCase(String name, String state, int crimeId, String location, Date date, Time time) {
		return autoExecute(AUTOEXECUTE_MODE.INSERT, Case.class, 
				"Name", name, 
				"Status", state,
				"CrimeId", crimeId,
				"Location", location,
				"Date", date,
				"time", time
		);
	}
	public Case updateCase(int CaseId, String name, String state, int crimeId, String location, Date date, Time time) {
		autoExecute(AUTOEXECUTE_MODE.UPDATE, Case.class, 
				"Name", name, 
				"Status", state,
				"CrimeId", crimeId,
				"Location", location,
				"Date", date,
				"time", time,
				"CaseId", CaseId
		);
		return getById(Long.valueOf(CaseId), Case.class);
	}
	
	public CaseNote insertComment(String comment, int caseId, int userid) {
		return autoExecute(AUTOEXECUTE_MODE.INSERT, CaseNote.class, 
				"CaseId", caseId, 
				"Note", comment,
				"UserId", userid
		);
	}

	public PoI insertPoI(String name, Date birthdate) {
		return autoExecute(AUTOEXECUTE_MODE.INSERT, PoI.class, 
				"Name", name, 
				"Birthdate", birthdate
		);
	}

	public PoINote insertPoINote(String comment, int poiId, int userid) {
		return autoExecute(AUTOEXECUTE_MODE.INSERT, PoINote.class, 
				"PoIId", poiId, 
				"Note", comment,
				"UserId", userid
		);
	}

	public <T extends Entity> T autoExecute(AUTOEXECUTE_MODE mode, Class<T> clazz, Object... fields) {
		if (fields.length % 2 != 0) {
			throw new IllegalArgumentException("Pair fieldnames and field values");
		}
		String tableName = getTableName(clazz);
		
		ArrayList<String> columnNames = new ArrayList<>();
		for (int i_columnName = 0; i_columnName < fields.length; i_columnName += 2) {
			String columnName = (String) fields[i_columnName];
			columnNames.add("`" + columnName + "`");
		}
		
		ArrayList<String> placeholders = new ArrayList<>();
		for (int i_data = 1; i_data < fields.length; i_data += 2) {
			Object data = fields[i_data];			
			placeholders.add(data == null ? "NULL" : "?");
		}
		
		try {
			PreparedStatement stmt;
			if (mode == AUTOEXECUTE_MODE.INSERT) {
				stmt = this.sqlConnection.prepareStatement("INSERT INTO " + tableName +" (" + StringUtils.join(columnNames, ", ") +") VALUES (" + StringUtils.join(placeholders, ", ") + ")", Statement.RETURN_GENERATED_KEYS);
			}
			else {
				StringBuilder sql = new StringBuilder();
				sql.append("UPDATE ");
				sql.append(tableName);
				sql.append(" SET ");
				for (int i = 0; i < columnNames.size() - 1; i++) {
					if (i > 0) {
						sql.append(", ");
					}
					sql.append(columnNames.get(i));
					sql.append("=");
					sql.append(placeholders.get(i));
				}
				sql.append(" WHERE ");
				sql.append(columnNames.get(columnNames.size()-1));
				sql.append("=");
				sql.append(placeholders.get(placeholders.size()-1));
				stmt = this.sqlConnection.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);
			}
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
			
			return getById(execute(stmt), clazz);
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

	public String getCasenameById(int id) {
		try (
				PreparedStatement stmt = this.sqlConnection.prepareStatement("SELECT `Name` FROM `Case` WHERE `CaseId` = " + id);
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
	public final List<Convict> getAllConvicts(Integer caseId) {
		String sql = "SELECT poi.*, cr.CrimeId, cr.Crime, c.CaseId, c.Date as ConvictionDate, c.Sentence  FROM poi " +
				"INNER JOIN Convicted c ON (poi.PoIId = c.PoIId AND c.CaseId=" + caseId + ") " +
				"INNER JOIN Crime cr ON (cr.CrimeId=c.CrimeId)";
		try (
			PreparedStatement stmt = 
			this.sqlConnection.prepareStatement(sql);
		) {
			return all(stmt, Convict.class);

		}  catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public final List<PoI> getAllPoIsNotLinked(Integer id) {
		String tableName = getTableName(PoI.class);
		String sql = "SELECT poi.* FROM " +
				tableName +  " poi LEFT JOIN Suspect s ON (poi.PoIId = s.PoIId AND s.CaseId=" + id + ") " +
				"WHERE IsNull(s.CaseId);";
		try (
			PreparedStatement stmt = 
			this.sqlConnection.prepareStatement(sql);
		) {
			return all(stmt, PoI.class);

		}  catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}



	public List<Conviction> getAllConvictions(Integer id) {
		String sql = " SELECT Convicted.*                "
		           + " FROM Convicted, PoI                "
		           + " WHERE                              "
		           + "     PoI.PoIId = " + id
		           + "     AND Convicted.PoIId = PoI.PoIId";
		try (
				//			this.sqlConnection.prepareStatement("SELECT Convicted.Date, Convicted.Sentence, Convicted.CaseId FROM" + 
				//			tableName +  ", `PoI` WHERE PoI.PoIId = '" + id + "' and PoI.PoIId = Convicted.PoIId;");
				// fix whatever is wrong with above statement
				PreparedStatement stmt = this.sqlConnection.prepareStatement(sql);
		) {
			return all(stmt, Conviction.class);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public void closeCase(int caseId) {
		runQuery("UPDATE `Case` SET status='closed' WHERE CaseId=" + caseId);
	}

	public void openCase(int caseId) {
		runQuery("UPDATE `Case` SET status='open' WHERE CaseId=" + caseId);
	}

	private void runQuery(String sql) {
		try (Statement stmt = sqlConnection.createStatement()) {
			stmt.execute(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	

	public Conviction insertConviction(int caseId, int poiId,
			Date convictionDate, String sentence, int crimeId) {
		try {
			PreparedStatement stmt = sqlConnection.prepareStatement("INSERT INTO Convicted (CaseId, PoIId, CrimeId, `Date`, Sentence) VALUES (?, ?, ?, ?, ?);");
			stmt.setInt(1, caseId);
			stmt.setInt(2, poiId);
			stmt.setInt(3, crimeId);
			stmt.setDate(4, convictionDate);
			stmt.setString(5, sentence);
			stmt.execute();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		
		try (
				Statement stmt = sqlConnection.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT * FROM Convicted WHERE CaseId=" + caseId + " AND PoIId=" + poiId);
			) {
				rs.next();
				return new Conviction(rs);
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
