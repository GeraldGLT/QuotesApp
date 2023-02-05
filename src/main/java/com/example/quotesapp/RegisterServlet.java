package com.example.quotesapp;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class RegisterServlet
*/
@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		String Name = request.getParameter("name");
		String Email = request.getParameter("email");
		String Password = request.getParameter("pass");
		String RePassword = request.getParameter("rePass");
		RequestDispatcher dispatcher = null;
		Connection con = null;
		
		if (Name == null || Name.equals("")) {
			request.setAttribute("status", "invalidName");
			dispatcher = request.getRequestDispatcher("register.jsp");
			dispatcher.forward(request, response);
		}
		if (Email == null || Email.equals("")) {
			request.setAttribute("status", "invalidEmail");
			dispatcher = request.getRequestDispatcher("register.jsp");
			dispatcher.forward(request,response);
		}
		if (Password == null || Password.equals("")) {
			request.setAttribute("status", "invalidPassword");
			dispatcher = request.getRequestDispatcher("register.jsp");
			dispatcher.forward(request,response);
		}else if(!Password.equals(RePassword)) {
			request.setAttribute("status", "invalidConfirmPassword");
			dispatcher = request.getRequestDispatcher("register.jsp");
			dispatcher.forward(request,response);
		}
			
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/User?useSSL=false", "root", "root");
			PreparedStatement pst = con.prepareStatement("insert into users(Name,Email,Password) values (?,?,?)");
			pst.setString(1, Name);
			pst.setString(2, Email);
			pst.setString(3, Password);
			
			int rowCount = pst.executeUpdate();
			dispatcher = request.getRequestDispatcher("register.jsp");
			
			if (rowCount > 0) {
				request.setAttribute("status", "success");
	
			} else {
				request.setAttribute("status","failed");
			}
			dispatcher.forward(request,  response);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				con.close();
			} catch (SQLException e) {
				System.out.println(e);
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}