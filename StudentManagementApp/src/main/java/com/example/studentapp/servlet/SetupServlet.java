package com.example.studentapp.servlet;

import com.example.studentapp.util.DBConnection;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

@WebServlet("/setup")
public class SetupServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");

        try (
            Connection con = DBConnection.getConnection();
            Statement st = con.createStatement()
        ) {
            // 1. Create database
            st.executeUpdate("CREATE DATABASE IF NOT EXISTS college_db");

            try (
                Connection dbCon = DBConnection.getConnectionToDatabase("college_db");
                Statement stmt = dbCon.createStatement()
            ) {
                // 2. Create users table
                stmt.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS users (" +
                    "id INT PRIMARY KEY AUTO_INCREMENT, " +
                    "username VARCHAR(50) NOT NULL UNIQUE, " +
                    "password VARCHAR(100) NOT NULL, " +
                    "role VARCHAR(20) NOT NULL)"
                );

                // 3. Create student table
                stmt.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS student (" +
                    "id INT PRIMARY KEY AUTO_INCREMENT, " +
                    "name VARCHAR(100) NOT NULL, " +
                    "email VARCHAR(120) NOT NULL UNIQUE, " +
                    "course VARCHAR(100) NOT NULL, " +
                    "photo_path VARCHAR(255), " +
                    "created_at DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                    "updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP)"
                );

                // 4. Create attendance table
                stmt.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS attendance (" +
                    "id INT PRIMARY KEY AUTO_INCREMENT, " +
                    "student_id INT NOT NULL, " +
                    "date DATE NOT NULL, " +
                    "status VARCHAR(20) NOT NULL, " +
                    "FOREIGN KEY (student_id) REFERENCES student(id) ON DELETE CASCADE)"
                );

                // 5. Create marks table
                stmt.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS marks (" +
                    "id INT PRIMARY KEY AUTO_INCREMENT, " +
                    "student_id INT NOT NULL, " +
                    "subject VARCHAR(100) NOT NULL, " +
                    "marks INT NOT NULL, " +
                    "FOREIGN KEY (student_id) REFERENCES student(id) ON DELETE CASCADE)"
                );

                // 6. Insert default users only if not already present
                insertUserIfNotExists(stmt, "admin", "admin123", "ADMIN");
                insertUserIfNotExists(stmt, "staff", "staff123", "STAFF");
                insertUserIfNotExists(stmt, "viewer", "viewer123", "VIEWER");
            }

            response.getWriter().println("<h2>Setup completed successfully.</h2>");
            response.getWriter().println("<p>Database and tables created.</p>");
            response.getWriter().println("<p>Default users created:</p>");
            response.getWriter().println("<ul>");
            response.getWriter().println("<li>admin / admin123 / ADMIN</li>");
            response.getWriter().println("<li>staff / staff123 / STAFF</li>");
            response.getWriter().println("<li>viewer / viewer123 / VIEWER</li>");
            response.getWriter().println("</ul>");
            response.getWriter().println("<br><a href='" + request.getContextPath() + "/login.jsp'>Go to Login</a>");

        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("<h2>Setup failed: " + e.getMessage() + "</h2>");
        }
    }

    private void insertUserIfNotExists(Statement stmt, String username, String password, String role) throws Exception {
        ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM users WHERE username='" + username + "'");
        rs.next();
        int count = rs.getInt(1);
        rs.close();

        if (count == 0) {
            stmt.executeUpdate(
                "INSERT INTO users(username, password, role) VALUES('" +
                username + "', '" + password + "', '" + role + "')"
            );
        }
    }
}