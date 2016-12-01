package aero.champ.projectpera.sql.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SqlServerConnectionManager {

	private static final String SQL_SERVER_DRIVER_STRING = "com.microsoft.sqlserver.jdbc.SQLServerDriver";

	private Connection conn;

	private String dbConnectionString;

	private String dbUserName;

	private String dbPassword;

	public SqlServerConnectionManager() {

	}

	public SqlServerConnectionManager(String dbConnectionString, String dbUserName, String dbPassword) {
		this.dbConnectionString = dbConnectionString;
		this.dbUserName = dbUserName;
		this.dbPassword = dbPassword;
	}

	public String getDbConnectionString() {
		return dbConnectionString;
	}

	public void setDbConnectionString(String dbConnectionString) {
		this.dbConnectionString = dbConnectionString;
	}

	public String getDbUserName() {
		return dbUserName;
	}

	public void setDbUserName(String dbUserName) {
		this.dbUserName = dbUserName;
	}

	public String getDbPassword() {
		return dbPassword;
	}

	public void setDbPassword(String dbPassword) {
		this.dbPassword = dbPassword;
	}

	public void dbConnect(String db_connect_string,
			String db_userid,
			String db_password)
	{
		try {
			Class.forName(SQL_SERVER_DRIVER_STRING);
			this.conn = DriverManager.getConnection(db_connect_string,
					db_userid, db_password);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public Connection getConn() {
		return this.conn;
	}	   

}
