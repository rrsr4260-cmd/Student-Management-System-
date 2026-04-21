package com.example.studentapp.servlet;

import com.example.studentapp.dao.AttendanceDAO;
import com.example.studentapp.dao.MarksDAO;
import com.example.studentapp.dao.StudentDAO;
import com.example.studentapp.model.Student;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            StudentDAO dao = new StudentDAO();
            int page = request.getParameter("page") == null ? 1 : Integer.parseInt(request.getParameter("page"));
            int pageSize = 5;
            String search = request.getParameter("search");

            List<Student> students = dao.getAll(page, pageSize, search);
            long total = dao.count(search);
            long totalPages = (long) Math.ceil((double) total / pageSize);

            request.setAttribute("students", students);
            request.setAttribute("page", page);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("search", search);
            request.setAttribute("totalStudents", dao.count(null));
            request.setAttribute("totalCourses", dao.countCourses());
            request.setAttribute("latestStudent", dao.latest());

            if (request.getParameter("editId") != null) {
                int id = Integer.parseInt(request.getParameter("editId"));
                request.setAttribute("editingStudent", dao.find(id));
                request.setAttribute("attendanceList", new AttendanceDAO().byStudent(id));
                request.setAttribute("marksList", new MarksDAO().byStudent(id));
            }

            request.getRequestDispatcher("/dashboard.jsp").forward(request, response);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}