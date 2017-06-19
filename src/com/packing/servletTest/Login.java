package com.packing.servletTest;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Login
 */
@WebServlet("/Login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
    public static final boolean DEPLOY_LOCAL = true;  
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Login() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// JDBC driver name and database URL
		final String JDBC_DRIVER="com.mysql.jdbc.Driver";  
	    String DB_URL="";
	      
	    // database user information
	      String USER = "";
	      String PSW = "";
		if (DEPLOY_LOCAL) {
			DB_URL="jdbc:mysql://localhost:3306/packingListApp";
		    USER = "myuser";
		    PSW = "xxxx";
		} else { // if deploy on Amazon AWS 
			DB_URL="aaxed7r2axc9ak.c54vncp087be.us-west-2.rds.amazonaws.com:3306";
			USER = "HYSQL1";
			PSW = "szzhy850420";
		}
		// set the type of response message
		response.setContentType("text/html");
		// get a output writer to write response message
		PrintWriter out = response.getWriter();
		// create a connection object for accessing database
		Connection loginConn = null;
		PreparedStatement loginStat = null;
		
		try {
			// register a JDBC driver
			Class.forName(JDBC_DRIVER);
			// open connection to mysql database userInfo
			System.out.println("Connecting to a database...");
			loginConn = DriverManager.getConnection(DB_URL, USER, PSW);
			System.out.println("Connected database successfully...");	
			
			// Execute SQL query
			String userQuery = "SELECT * FROM CustomerInfo WHERE email=?";
			System.out.println("Creating statement...");
		    loginStat =loginConn.prepareStatement(userQuery);
		    
		    // get user email
		    loginStat.setString(1, request.getParameter("userEmail"));
		    ResultSet emailResult = loginStat.executeQuery();
		    
		    // if email is existing in the data base
		    if (emailResult.next()) {
		    	loginStat.close();
		    	loginStat = loginConn.prepareStatement("SELECT * FROM CustomerInfo WHERE email=? and password=?");
		    	loginStat.setString(1, request.getParameter("userEmail"));
		    	loginStat.setString(2, request.getParameter("psw"));
		    	ResultSet result = loginStat.executeQuery();
		    
		    	if (result.next()) {
		    		// login successfully, redirect user to create the list	
		    		int userID = result.getInt("usrID"); // TODO: to be modified. cannot extract userID. Try JS
		    		request.setAttribute("usrID", userID);
		    		System.out.println(userID);
		    		RequestDispatcher rd = request.getRequestDispatcher("createList.html");
		    		rd.forward(request, response);
		    	} else { // email and/or password are/is wrong
		    		// alert user entering wrong login information. display the login page again
		    		out.println("<html><body><script>alert('Invalid email or password, make sure you enter them right');</script></body></html>");
			    	RequestDispatcher rd = request.getRequestDispatcher("login.html");
			        rd.include(request, response);
		    	}
		    }else { // email is not existing
		    	// fail to login, redirect to login page
		    	 out.println("<html><body><script>alert('Invalid email or password, make sure you enter them right');</script></body></html>");
		    	 RequestDispatcher rd = request.getRequestDispatcher("login.html");
		         rd.include(request, response);
		    }
		    
			
		} catch (ClassNotFoundException ex) {
			System.out.println("Error: unable to load driver class!");
			System.exit(1);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
	         out.close();
	         try {
	            // Close the Statement and Connection
	            if (loginStat != null) loginStat.close();
	            if (loginConn != null) loginConn.close();
	         } catch (SQLException ex) {
	            ex.printStackTrace();
	         }
	      }
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
