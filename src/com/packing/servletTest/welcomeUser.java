package com.packing.servletTest;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class welcomeUser
 */
@WebServlet("/welcomeUser")
public class welcomeUser extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final boolean DEPLOY_LOCAL = true;  
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public welcomeUser() {
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
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
