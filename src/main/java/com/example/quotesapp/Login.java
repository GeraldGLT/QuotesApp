package com.example.quotesapp;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class Login
 */
@WebServlet("/Login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	
		String Email = request.getParameter("email");
		String Password = request.getParameter("pass");
		HttpSession session = request.getSession();
		RequestDispatcher dispatcher = null;
		
		if(Email == null || Email.equals("")) {
			session.setAttribute("status", "invalidEmail");
			dispatcher = request.getRequestDispatcher("login.jsp");
			dispatcher.forward(request, response);
		}
		if(Password == null || Password.equals("")) {
			session.setAttribute("status", "invalidPassword");
			dispatcher = request.getRequestDispatcher("login.jsp");
			dispatcher.forward(request, response);
		}
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/User?useSSL=false", "root", "root");
			PreparedStatement pst = con.prepareStatement("select * from User where Email = ? and Password = ?");
			pst.setString(1, Email);
			pst.setString(2, Password);
			
			ResultSet rs = pst.executeQuery();
			if (rs.next()) {
				session.setAttribute("Email", rs.getString("Email"));
				dispatcher = request.getRequestDispatcher("index.jsp");
			} else {
				request.setAttribute("status", "failed");
				dispatcher = request.getRequestDispatcher("login.jsp");
			}
			dispatcher.forward(request,  response);
			
		} catch (Exception e) {
			e.printStackTrace();
			
		}
	}
}
