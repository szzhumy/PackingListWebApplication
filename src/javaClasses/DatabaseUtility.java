package javaClasses;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;


public class DatabaseUtility {
	private final String JDBC_DRIVER="com.mysql.jdbc.Driver";
	private String dbUrl;
	private String dbUser;
	private String dbPassword;
	
	// constructor only visible to its sub-classes
	protected DatabaseUtility(boolean deployLocal) {
		if (deployLocal) {
			dbUrl="jdbc:mysql://localhost:3306/packingListApp";
		    dbUser = "myuser";
		    dbPassword = "xxxx";
		} else { // if deploy on Amazon AWS 
			dbUrl="aaxed7r2axc9ak.c54vncp087be.us-west-2.rds.amazonaws.com:3306";
			dbUser = "HYSQL1";
			dbPassword = "szzhy850420";
		}
	}
	
	// start a connection to access MySQL database
	protected Connection getNewConnection() throws ClassNotFoundException, SQLException {
		// register a JDBC driver
		Class.forName(JDBC_DRIVER);
		// open connection to a certain MySQL database
		System.out.println("Connecting to database" + dbUrl + " ...");
		Connection con = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
		System.out.println("Connected to the database successfully...");
		return con;
	}
	
	private void closeConnectionIfNeeded(Connection con) throws SQLException {
		if(con != null) {
			con.close();
		}
	}
	
	private void closeStatementIfNeeded(PreparedStatement stat) throws SQLException {
		if(stat != null) {
			stat.close();
		}
	}
	
	protected void closeConnectionAndStatement(Connection con, PreparedStatement stat) throws SQLException {
		closeConnectionIfNeeded(con);
		closeStatementIfNeeded(stat);
	}
	
	// execute a SQL query
	public ResultSet executeAQuery(Connection con, PreparedStatement stat, String[] parameters)
			throws SQLException {		
		// declare a set to store the result extract from database by executing a SQL query
		ResultSet result;
		for (int i = 0; i < parameters.length; i++) {
			stat.setString(i + 1, parameters[i]);
		}
		// store the results to the ResultSet
		result = stat.executeQuery();
		return result;
	}
	
	// TODO write this
	public boolean checkIfValueExist (String tableName, String[] columnNames, String[] columnValues)
			throws ClassNotFoundException, SQLException {
		return false;
	}
	
	// TODO, uncomment later
	/*
	public boolean checkIfValueExist (String tableName, String columnName, String columnValue)
			throws ClassNotFoundException, SQLException {
		String[] columnNames = {columnName};
		String[] columnValues = {columnValue};
		return checkIfValueExist(tableName, columnNames, columnValues);
	}
	*/
	
	public boolean checkIfValueExist (String tableName, String columnName, String columnValue) throws ClassNotFoundException, SQLException {
		// check if information entered by user matches the information in the database
		// call superclass method executeAQuery()
		Connection con = null;
		PreparedStatement stat = null;
		try {
			con = getNewConnection();
			stat = con.prepareStatement("select * from " + tableName + " where " + columnName + " = ?");
			String[] parameters = {columnValue};
			ResultSet result = executeAQuery(con, stat, parameters);
			if (result.next()) { // information matches
				return true;
			}
		} finally {
			closeConnectionAndStatement(con, stat);
		}
		// information does not match
		return false;
	}
	
	public static void insertInfoIntoTable(HttpServletRequest req, Connection conn, PreparedStatement stat, String sql, int numbersOfParameters, String parametersStr) {
		// set the parameters for a query statement by calling help method setParameters
		String[] parameters = setParameters(numbersOfParameters, parametersStr);
		try {
		System.out.println("Inserting info into the table... ");
		stat = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
		// add information one by one into the table
		for (int i = 1; i <= numbersOfParameters; i++) {
			stat.setString(i, req.getParameter(parameters[i]));
		}
		stat.executeUpdate();
		stat.close();// TODO: confirm if this is necessary
		
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private static String[] setParameters(int numbersOfParameters, String str) {
		String[] parameters = new String[numbersOfParameters];
		int index = 1;
		StringBuilder temp = new StringBuilder();
		char[] strAry = str.toCharArray();
		for (int i = 1; i <= str.length(); i++) {
			if (strAry[i] == ',' || i == str.length()) {
				parameters[index] = temp.toString();
				index++;
			} else {
				temp.append(strAry[i]);				
			}
		}
		return parameters;
	}
}
