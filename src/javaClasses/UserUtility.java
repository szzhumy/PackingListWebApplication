package javaClasses;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.packing.servletTest.Login;

// TODO make everything un-static
public class UserUtility extends DatabaseUtility {
	private final static String TABLE_NAME = "CustomerInfo";
	private final static UserUtility INSTANCE = new UserUtility();

	private UserUtility() {
		super(Login.DEPLOY_LOCAL);
	}
	
	// To use this outside, use this example
	// UserUtility.get().addUser()
	public static UserUtility get() {
		return INSTANCE;
	}
	
	// TODO: rewrite. shouldn't handle certain condition in here
	// this method create a new user in the MySQL database
	public static boolean addUser(User user) throws SQLException, ServletException, IOException {
		// check if email entered is already existing by calling help method checkIfInfoEnteredMatchInfoInSystem()
		/*if (UserUtility.get().checkIfEmailExist(user.getEmail())) { // email exists
			// pop an alert window to info user the email is already in the system
			out.println("<html><body><script>alert('Email is already existing!');</script></body></html>");
			// redirect user back to login/create new user page
			RequestDispatcher rd = req.getRequestDispatcher("login.html");
			rd.include(req, response);
			return false;
		} else { // if email entered is not in the system, insert the new user info into the database
			// first check if passwords entered are match
			if (passwordConfirmed(req, stat, passwordParameter, password2Parameter)) {
			insertInfoIntoTable(req, conn, stat, sql, numbersOfParameters, parametersStr);
			return true;
			} else { // passwords are entered differently
				// pop an alert window to warn user about different passwords entered
				out.println("<html><body><script>alert('Passwords are different');</script></body></html>");
				// redirect user back to login/create new user page
				RequestDispatcher rd = req.getRequestDispatcher("login.html");
				rd.include(req, response);
				return false;
			}
		}
		*/
		return false;
	}
	
	// TODO: rewrite the function
	public static boolean loginUser(HttpServletRequest req, HttpServletResponse response, String sql, PreparedStatement stat, Connection conn, int numbersOfParameters, String parametersString, PrintWriter out) throws ServletException, IOException, SQLException{
		// if email and password entered matches the information in the database, 
		// numbersOfParameters will be 2, parameterString is the parameters of email and password
		//if (checkIfInfoEnteredMatchInfoInSystem(req, conn, stat, sql, numbersOfParameters, parametersString )) {
			// redirect user to createList page
			RequestDispatcher rd = req.getRequestDispatcher("createList.html");
    		rd.forward(req, response);
			return true;
		//} else { // email is not existing or password doesn't match
	    	// fail to login, redirect to login page
			// pop an alert window to info the user login is failed
	    //	  out.println("<html><body><script>alert('Invalid email or password, make sure you enter them right');</script></body></html>");
	    //	 RequestDispatcher rd = req.getRequestDispatcher("login.html");
	    //     rd.include(req, response);
	     //    return false;
	    //}
		
	}
	
	public static boolean updateUser() {
		return false;
	}
	
	public static boolean deleteUser() {
		return false;
	}
	
	public boolean checkIfEmailExist (String emailAddress) throws ClassNotFoundException, SQLException {
		return checkIfValueExist(TABLE_NAME, "email", emailAddress);
	}
	
	private static boolean passwordConfirmed(HttpServletRequest req, PreparedStatement stat, String passwordParameter, String password2Parameter) {
		
		if (req.getParameter(passwordParameter).equals(req.getParameter(password2Parameter))) {
			return true;
		} else {
		return false;
		}
	}
	
	
	
}
