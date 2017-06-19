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
 * Servlet implementation class CreateNewUser
 */
@WebServlet("/CreateNewUser")
public class CreateNewUser extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final boolean DEPLOY_LOCAL = true; 
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CreateNewUser() {
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
	    // get an output writer to write response message
	      PrintWriter out = response.getWriter();
	    // create a connection object and a statement object
	      Connection createUserConn = null;
	      PreparedStatement createUserStat = null;
	      try {
	    	  // register a JDBC driver
	    	  Class.forName(JDBC_DRIVER);
	    	  // open connection to database
	    	  System.out.println("Connecting to MySQL database...");
	    	  createUserConn = DriverManager.getConnection(DB_URL, USER, PSW);
	    	  System.out.println("Connected database successfully...");
	    	  
	    	  // execute SQL query
	    	  // check if email is already existing. if so remind user to enter the right email
	    	  String userQuery1 = "SELECT * FROM CustomerInfo WHERE email=?";
	    	  createUserStat = createUserConn.prepareStatement(userQuery1);
	    	  createUserStat.setString(1, request.getParameter("email"));
	    	  ResultSet emailFind = createUserStat.executeQuery();
	    	  if (emailFind.next()) { // email is already in the database
	    		  createUserStat.close();
	    		  out.println("<html><body><script>alert('Email is already existing!');</script></body></html>");
	    		  RequestDispatcher rd = request.getRequestDispatcher("login.html");
	    		  rd.include(request, response);
	    		  
	    	  } else {// if email is not existing, insert the new user into database
	    		  createUserStat.close();
	    		  String userQuery = "INSERT INTO CustomerInfo(usrID, email, firstName, lastName, password) VALUES(null,?,?,?,?)";
	    		  System.out.println("Creating a new user...");
	    		  createUserStat = createUserConn.prepareStatement(userQuery, PreparedStatement.RETURN_GENERATED_KEYS);
	    		  createUserStat.setString(1, request.getParameter("email"));
	    		  createUserStat.setString(2, request.getParameter("firstName"));
	    		  createUserStat.setString(3, request.getParameter("lastName"));
	    		  
	    		  // check if passwords entered are match, if so, create user successfully
	    		  if (request.getParameter("newPsw").equals(request.getParameter("confirmPsw"))) {
	    			  createUserStat.setString(4, request.getParameter("newPsw"));
	    		  } else { // if passwords are entered differently, send alert message to user 
	    			  out.println("<html><body><script>alert('Passwords are different');</script></body></html>");
	    			  RequestDispatcher rd = request.getRequestDispatcher("login.html");
	    			  rd.include(request, response);
	    		  }
	    		  createUserStat.executeUpdate();
	    		  
	    		  ResultSet id = createUserStat.getGeneratedKeys();
	    		  id.next();
	    		  int userID = id.getInt(1);
	    		  System.out.println("THE USER ID IS: " + userID);
	    		  request.setAttribute("usrID", userID);
	    		  RequestDispatcher rd = request.getRequestDispatcher("createList.html");
	    		  rd.forward(request, response);
	    	  }
	    	  
	      } catch (ClassNotFoundException ex) {
				System.out.println("Error: unable to load driver class!");
				System.exit(1);
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
		         out.close();
		         try {
		            // Step 5: Close the Statement and Connection
		            if (createUserStat != null) createUserStat.close();
		            if (createUserConn != null) createUserConn.close();
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
