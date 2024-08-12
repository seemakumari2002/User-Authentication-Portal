package com.uniquedevloper.registration;



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

import com.uniquedevloper.db.DbConnection;

@WebServlet("/register")
public class RegistrationServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	PrintWriter out = response.getWriter();
        String uname = request.getParameter("name");
        String uemail = request.getParameter("email");
        String upwd = request.getParameter("pass");
        String reupwd = request.getParameter("re_pass");
        String umobile = request.getParameter("contact");
        RequestDispatcher dispatcher = null;
        Connection con = null;

        // Validation logic
        if (uname == null || uname.isEmpty()) {
            request.setAttribute("status", "invalidName");
            dispatcher = request.getRequestDispatcher("registration.jsp");
            if (dispatcher != null) {
                dispatcher.forward(request, response);
            }
            return;
        }
        if (uemail == null || uemail.isEmpty()) {
            request.setAttribute("status", "invalidEmail");
            dispatcher = request.getRequestDispatcher("registration.jsp");
            if (dispatcher != null) {
                dispatcher.forward(request, response);
            }
            return;
        }
        if (upwd == null || upwd.isEmpty()) {
            request.setAttribute("status", "invalidUpwd");
            dispatcher = request.getRequestDispatcher("registration.jsp");
            if (dispatcher != null) {
                dispatcher.forward(request, response);
            }
            return;
        }
        if (!upwd.equals(reupwd)) {
            request.setAttribute("status", "invalidConfirmPassword");
            dispatcher = request.getRequestDispatcher("registration.jsp");
            if (dispatcher != null) {
                dispatcher.forward(request, response);
            }
            return;
        }
        if (umobile == null || umobile.isEmpty()) {
            request.setAttribute("status", "invalidMobile");
            dispatcher = request.getRequestDispatcher("registration.jsp");
            if (dispatcher != null) {
                dispatcher.forward(request, response);
            }
            return;
        }
        if (umobile.length() > 10) {
            request.setAttribute("status", "invalidMobileLength");
            dispatcher = request.getRequestDispatcher("registration.jsp");
            if (dispatcher != null) {
                dispatcher.forward(request, response);
            }
            return;
        }

        try {
        	con=DbConnection.getConnection("jdbc:mysql://localhost:3306","user_db","root","root");
             PreparedStatement pst = con.prepareStatement("INSERT INTO users (uname, upwd, uemail, umobile) VALUES (?, ?, ?, ?)");
            pst.setString(1, uname);
            pst.setString(2, upwd);
            pst.setString(3, uemail);
            pst.setString(4, umobile);

            int rowCount = pst.executeUpdate();
            dispatcher = request.getRequestDispatcher("registration.jsp");
            if (rowCount > 0) {
                request.setAttribute("status", "success");
            } else {
                request.setAttribute("status", "failed");
            }
            
            if (dispatcher != null) {
                dispatcher.forward(request, response); // This is line 102
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
