package com.example.studentapp.servlet;

import com.example.studentapp.dao.StudentDAO;
import com.example.studentapp.model.Student;
import com.example.studentapp.util.EmailUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

@WebServlet("/student")
@MultipartConfig
public class StudentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    private boolean canWrite(HttpServletRequest request) {
        String role = (String) request.getSession().getAttribute("role");
        return "ADMIN".equals(role) || "STAFF".equals(role);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if (!canWrite(request)) {
            response.getWriter().println("Access denied");
            return;
        }

        String action = request.getParameter("action");
        StudentDAO dao = new StudentDAO();

        try {
            if ("insert".equals(action)) {
                String name = request.getParameter("name");
                String email = request.getParameter("email");
                String course = request.getParameter("course");
                validate(name, email, course);

                if (dao.findByEmail(email) != null) throw new RuntimeException("Email already exists");

                String photoPath = savePhoto(request);
                dao.save(new Student(name.trim(), email.trim(), course.trim(), photoPath));
                EmailUtil.sendStudentWelcomeEmail(email.trim(), name.trim());

            } else if ("update".equals(action)) {
                int id = Integer.parseInt(request.getParameter("id"));
                Student s = dao.find(id);
                if (s == null) throw new RuntimeException("Student not found");

                String name = request.getParameter("name");
                String email = request.getParameter("email");
                String course = request.getParameter("course");
                validate(name, email, course);

                Student emailOwner = dao.findByEmail(email.trim());
                if (emailOwner != null && emailOwner.getId() != id) throw new RuntimeException("Another student already uses this email");

                String photoPath = savePhoto(request);
                s.setName(name.trim());
                s.setEmail(email.trim());
                s.setCourse(course.trim());
                if (photoPath != null) s.setPhotoPath(photoPath);

                dao.update(s);

            } else if ("delete".equals(action)) {
                if (!"ADMIN".equals(request.getSession().getAttribute("role"))) {
                    response.getWriter().println("Only ADMIN can delete");
                    return;
                }
                int id = Integer.parseInt(request.getParameter("id"));
                dao.delete(id);
            }
        } catch (Exception e) {
            request.getSession().setAttribute("flashMessage", e.getMessage());
            request.getSession().setAttribute("flashType", "error");
            response.sendRedirect(request.getContextPath() + "/dashboard");
            return;
        }

        request.getSession().setAttribute("flashMessage", "Operation completed successfully");
        request.getSession().setAttribute("flashType", "success");
        response.sendRedirect(request.getContextPath() + "/dashboard");
    }

    private void validate(String name, String email, String course) {
        if (name == null || name.trim().isEmpty()) throw new RuntimeException("Name is required");
        if (email == null || email.trim().isEmpty()) throw new RuntimeException("Email is required");
        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) throw new RuntimeException("Invalid email");
        if (course == null || course.trim().isEmpty()) throw new RuntimeException("Course is required");
    }

    private String savePhoto(HttpServletRequest request) throws IOException, ServletException {
        Part filePart = request.getPart("photo");
        if (filePart == null || filePart.getSize() == 0) return null;

        String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
        String uploadDir = request.getServletContext().getRealPath("") + File.separator + "uploads";
        File dir = new File(uploadDir);
        if (!dir.exists()) dir.mkdirs();

        String savedName = System.currentTimeMillis() + "_" + fileName;
        filePart.write(uploadDir + File.separator + savedName);
        return "uploads/" + savedName;
    }
}