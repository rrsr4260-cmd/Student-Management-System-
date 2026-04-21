package com.example.studentapp.servlet;

import com.example.studentapp.dao.AttendanceDAO;
import com.example.studentapp.dao.MarksDAO;
import com.example.studentapp.model.Attendance;
import com.example.studentapp.model.Marks;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.time.LocalDate;

@WebServlet("/academic")
public class MarksServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    private boolean canWrite(HttpServletRequest request) {
        String role = (String) request.getSession().getAttribute("role");
        return "ADMIN".equals(role) || "STAFF".equals(role);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (!canWrite(request)) {
            response.getWriter().println("Access denied");
            return;
        }

        String action = request.getParameter("action");
        int studentId = Integer.parseInt(request.getParameter("studentId"));

        try {
            if ("attendance".equals(action)) {
                LocalDate date = LocalDate.parse(request.getParameter("date"));
                String status = request.getParameter("status");
                new AttendanceDAO().save(new Attendance(studentId, date, status));
            } else if ("marks".equals(action)) {
                String subject = request.getParameter("subject");
                int marks = Integer.parseInt(request.getParameter("marks"));
                new MarksDAO().save(new Marks(studentId, subject, marks));
            }
            request.getSession().setAttribute("flashMessage", "Academic record saved");
            request.getSession().setAttribute("flashType", "success");
        } catch (Exception e) {
            request.getSession().setAttribute("flashMessage", e.getMessage());
            request.getSession().setAttribute("flashType", "error");
        }

        response.sendRedirect(request.getContextPath() + "/dashboard?editId=" + studentId);
    }
}