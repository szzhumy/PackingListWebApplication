package com.packing.servletTest;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class GetInfoFromForm
 */
@WebServlet("/GetInfoFromForm")
public class GetInfoFromForm extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static final boolean DEPLOY_LOCAL = true; 
    /**
     * Default constructor. 
     */
    public GetInfoFromForm() {
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
		// set the type of the response message
		response.setContentType("text/html");  
		// get a output writer to write the response message
		PrintWriter out = response.getWriter();  
		// create a connection object and a statement object
		Connection conn = null;
		PreparedStatement stat = null;
		
		try {
			// register a JDBC Driver
			Class.forName(JDBC_DRIVER);
			
			// open a connection to MySQL database
			System.out.println("Connecting to a database...");
			conn = DriverManager.getConnection(DB_URL, USER, PSW);
			System.out.println("Connected database successfully...");	
			
			String sql = "INSERT INTO tripDetails(tripID, usrID, tripDate, tripType, transportationType) " +
                   "VALUES (null, ?, ?, ?, ?)";
			System.out.println("Creating a statement...");
			stat = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
			stat.setInt(1, (Integer)request.getAttribute("usrID"));
			stat.setString(2, request.getParameter("tripDate"));
			stat.setString(3, request.getParameter("isBizTrip"));
			stat.setString(4, request.getParameter("transportation"));
			
			stat.executeUpdate();
			
		} catch(ClassNotFoundException ex) {
			   System.out.println("Error: unable to load driver class!");
			   System.exit(1);
		}
		catch (SQLException e) {
			e.printStackTrace();
		} 
		finally {
	         out.close();
	         try {
	            // Step 5: Close the Statement and Connection
	            if (stat != null) stat.close();
	            if (conn != null) conn.close();
	         } catch (SQLException ex) {
	            ex.printStackTrace();
	         }
	      }
		
		String username = request.getParameter("name");    
         
        // build HTML code
        String htmlRespone = "<html>";
        htmlRespone += "<h2>Your username is: " + username + "<br/>";      
        htmlRespone += "</h2>";    
        htmlRespone += "</html>";
         
        // return response
        out.println(htmlRespone);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
