<%@ page import="java.util.*" %>
<%@ page import="com.example.studentapp.model.Student" %>
<%@ page import="com.example.studentapp.model.Attendance" %>
<%@ page import="com.example.studentapp.model.Marks" %>
<%
    @SuppressWarnings("unchecked")
    List<Student> students = (List<Student>) request.getAttribute("students");

    Student editingStudent = (Student) request.getAttribute("editingStudent");

    @SuppressWarnings("unchecked")
    List<Attendance> attendanceList = (List<Attendance>) request.getAttribute("attendanceList");

    @SuppressWarnings("unchecked")
    List<Marks> marksList = (List<Marks>) request.getAttribute("marksList");

    Long totalStudents = (Long) request.getAttribute("totalStudents");
    Long totalCourses = (Long) request.getAttribute("totalCourses");
    Student latestStudent = (Student) request.getAttribute("latestStudent");
    Integer pageNo = (Integer) request.getAttribute("page");
    Long totalPages = (Long) request.getAttribute("totalPages");
    String search = (String) request.getAttribute("search");

    String username = (String) session.getAttribute("username");
    String role = (String) session.getAttribute("role");
    String flashMessage = (String) session.getAttribute("flashMessage");
    String flashType = (String) session.getAttribute("flashType");
    session.removeAttribute("flashMessage");
    session.removeAttribute("flashType");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Dashboard - Student Management</title>
    <link rel="stylesheet" href="css/style.css">
    <script src="js/app.js" defer></script>
</head>
<body>
<div class="page">
    <aside class="sidebar">
        <div class="brand">
            <h1>StudentMS</h1>
            <p>Welcome, <b><%= username %></b></p>
            <p>Role: <b><%= role %></b></p>
        </div>

        <div class="side-card">
            <h3>Stats</h3>
            <p>Total Students: <strong><%= totalStudents != null ? totalStudents : 0 %></strong></p>
            <p>Total Courses: <strong><%= totalCourses != null ? totalCourses : 0 %></strong></p>
            <p>Latest Student: <strong><%= latestStudent != null ? latestStudent.getName() : "N/A" %></strong></p>
        </div>

        <div class="side-card">
            <h3>Exports</h3>
            <a class="btn btn-primary full-btn mb" href="export?type=csv">CSV Export</a>
            <a class="btn btn-success full-btn mb" href="export?type=excel">Excel Export</a>
            <a class="btn btn-warning full-btn" href="export?type=pdf">PDF Export</a>
        </div>

        <div class="side-card">
            <h3>Session</h3>
            <a class="btn btn-dark full-btn" href="logout">Logout</a>
        </div>
    </aside>

    <main class="main-content">
        <div class="hero">
            <h2>Student Management Dashboard</h2>
            <p>Professional 3D panel with authentication, pagination, exports, photo upload, attendance, marks, and analytics.</p>
        </div>

        <% if (flashMessage != null) { %>
            <div class="alert <%= "success".equals(flashType) ? "alert-success" : "alert-error" %>"><%= flashMessage %></div>
        <% } %>

        <section class="card">
            <div class="chart-grid">
                <div class="chart-card">
                    <h3>Students vs Courses</h3>
                    <canvas id="barChart"
                            data-students="<%= totalStudents != null ? totalStudents : 0 %>"
                            data-courses="<%= totalCourses != null ? totalCourses : 0 %>"></canvas>
                </div>
            </div>
        </section>

        <% if (!"VIEWER".equals(role)) { %>
        <div class="form-grid">
            <section class="card">
                <h3>Add Student</h3>
                <form action="student" method="post" enctype="multipart/form-data">
                    <input type="hidden" name="action" value="insert">
                    <div class="form-group"><label>Name</label><input type="text" name="name" required></div>
                    <div class="form-group"><label>Email</label><input type="email" name="email" required></div>
                    <div class="form-group"><label>Course</label><input type="text" name="course" required></div>
                    <div class="form-group"><label>Photo</label><input type="file" name="photo" accept="image/*"></div>
                    <button class="btn btn-success" type="submit">Insert Student</button>
                </form>
            </section>

            <section class="card">
                <h3><%= editingStudent != null ? "Edit Student" : "Select Edit from Table" %></h3>
                <form action="student" method="post" enctype="multipart/form-data">
                    <input type="hidden" name="action" value="update">
                    <div class="form-group"><label>ID</label><input type="number" name="id" value="<%= editingStudent != null ? editingStudent.getId() : "" %>" required></div>
                    <div class="form-group"><label>Name</label><input type="text" name="name" value="<%= editingStudent != null ? editingStudent.getName() : "" %>" required></div>
                    <div class="form-group"><label>Email</label><input type="email" name="email" value="<%= editingStudent != null ? editingStudent.getEmail() : "" %>" required></div>
                    <div class="form-group"><label>Course</label><input type="text" name="course" value="<%= editingStudent != null ? editingStudent.getCourse() : "" %>" required></div>
                    <div class="form-group"><label>Photo</label><input type="file" name="photo" accept="image/*"></div>
                    <button class="btn btn-warning" type="submit">Update Student</button>
                </form>
            </section>
        </div>
        <% } %>

        <section class="card">
            <div class="table-header">
                <h3>Student Records</h3>
                <form class="search-form" method="get" action="dashboard">
                    <input type="text" name="search" placeholder="Search name, email, course" value="<%= search != null ? search : "" %>">
                    <button type="submit" class="btn btn-primary">Search</button>
                    <a href="dashboard" class="btn btn-dark">Reset</a>
                </form>
            </div>

            <div class="table-wrap">
                <table>
                    <thead>
                    <tr>
                        <th>ID</th><th>Photo</th><th>Name</th><th>Email</th><th>Course</th><th>Created</th><th>Actions</th>
                    </tr>
                    </thead>
                    <tbody>
                    <% if (students != null && !students.isEmpty()) {
                        for (Student s : students) { %>
                        <tr>
                            <td><%= s.getId() %></td>
                            <td>
                                <% if (s.getPhotoPath() != null) { %>
                                    <img src="<%= s.getPhotoPath() %>" class="student-photo" alt="photo">
                                <% } else { %>N/A<% } %>
                            </td>
                            <td><%= s.getName() %></td>
                            <td><%= s.getEmail() %></td>
                            <td><%= s.getCourse() %></td>
                            <td><%= s.getCreatedAt() %></td>
                            <td class="actions">
                                <a class="btn btn-warning small-btn" href="dashboard?editId=<%= s.getId() %>">Edit</a>
                                <% if (!"VIEWER".equals(role)) { %>
                                <a class="btn btn-primary small-btn" href="dashboard?editId=<%= s.getId() %>">Academic</a>
                                <% } %>
                                <% if ("ADMIN".equals(role)) { %>
                                <form class="inline-form" action="student" method="post" onsubmit="return confirmDelete('Delete this student?')">
                                    <input type="hidden" name="action" value="delete">
                                    <input type="hidden" name="id" value="<%= s.getId() %>">
                                    <button class="btn btn-danger small-btn" type="submit">Delete</button>
                                </form>
                                <% } %>
                            </td>
                        </tr>
                    <% }} else { %>
                        <tr><td colspan="7" class="no-data">No records found</td></tr>
                    <% } %>
                    </tbody>
                </table>
            </div>

            <div class="pagination">
                <% for (int i = 1; i <= (totalPages == null ? 1 : totalPages); i++) { %>
                    <a class="page-link <%= (pageNo != null && pageNo == i) ? "active" : "" %>"
                       href="dashboard?page=<%= i %><%= search != null ? "&search=" + search : "" %>"><%= i %></a>
                <% } %>
            </div>
        </section>

        <% if (editingStudent != null && !"VIEWER".equals(role)) { %>
        <div class="form-grid">
            <section class="card">
                <h3>Attendance for <%= editingStudent.getName() %></h3>
                <form action="academic" method="post">
                    <input type="hidden" name="action" value="attendance">
                    <input type="hidden" name="studentId" value="<%= editingStudent.getId() %>">
                    <div class="form-group"><label>Date</label><input type="date" name="date" required></div>
                    <div class="form-group">
                        <label>Status</label>
                        <select name="status">
                            <option value="PRESENT">PRESENT</option>
                            <option value="ABSENT">ABSENT</option>
                        </select>
                    </div>
                    <button class="btn btn-success" type="submit">Save Attendance</button>
                </form>

                <div class="mini-list">
                    <% if (attendanceList != null) for (Attendance a : attendanceList) { %>
                        <div><%= a.getDate() %> - <%= a.getStatus() %></div>
                    <% } %>
                </div>
            </section>

            <section class="card">
                <h3>Marks for <%= editingStudent.getName() %></h3>
                <form action="academic" method="post">
                    <input type="hidden" name="action" value="marks">
                    <input type="hidden" name="studentId" value="<%= editingStudent.getId() %>">
                    <div class="form-group"><label>Subject</label><input type="text" name="subject" required></div>
                    <div class="form-group"><label>Marks</label><input type="number" name="marks" min="0" max="100" required></div>
                    <button class="btn btn-primary" type="submit">Save Marks</button>
                </form>

                <div class="mini-list">
                    <% if (marksList != null) for (Marks m : marksList) { %>
                        <div><%= m.getSubject() %> - <%= m.getMarks() %></div>
                    <% } %>
                </div>
            </section>
        </div>
        <% } %>
    </main>
</div>

<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
</body>
</html>