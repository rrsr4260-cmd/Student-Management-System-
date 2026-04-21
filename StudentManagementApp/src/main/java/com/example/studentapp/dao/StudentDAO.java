package com.example.studentapp.dao;

import com.example.studentapp.model.Student;
import com.example.studentapp.util.DBConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {

    public void save(Student student) {
        String sql = "INSERT INTO student(name, email, course, photo_path) VALUES (?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnectionToDatabase("college_db");
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, student.getName());
            ps.setString(2, student.getEmail());
            ps.setString(3, student.getCourse());
            ps.setString(4, student.getPhotoPath());
            ps.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException("Insert failed: " + e.getMessage(), e);
        }
    }

    public void update(Student student) {
        String sql = "UPDATE student SET name=?, email=?, course=?, photo_path=? WHERE id=?";

        try (Connection con = DBConnection.getConnectionToDatabase("college_db");
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, student.getName());
            ps.setString(2, student.getEmail());
            ps.setString(3, student.getCourse());
            ps.setString(4, student.getPhotoPath());
            ps.setInt(5, student.getId());
            ps.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException("Update failed: " + e.getMessage(), e);
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM student WHERE id=?";

        try (Connection con = DBConnection.getConnectionToDatabase("college_db");
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException("Delete failed: " + e.getMessage(), e);
        }
    }

    public Student find(int id) {
        String sql = "SELECT * FROM student WHERE id=?";

        try (Connection con = DBConnection.getConnectionToDatabase("college_db");
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapStudent(rs);
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("Find failed: " + e.getMessage(), e);
        }

        return null;
    }

    public Student findByEmail(String email) {
        String sql = "SELECT * FROM student WHERE email=?";

        try (Connection con = DBConnection.getConnectionToDatabase("college_db");
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, email);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapStudent(rs);
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("Find by email failed: " + e.getMessage(), e);
        }

        return null;
    }

    public List<Student> getAll(int page, int pageSize, String keyword) {
        List<Student> list = new ArrayList<>();
        String sql;

        boolean hasKeyword = keyword != null && !keyword.trim().isEmpty();

        if (hasKeyword) {
            sql = "SELECT * FROM student " +
                  "WHERE LOWER(name) LIKE ? OR LOWER(email) LIKE ? OR LOWER(course) LIKE ? " +
                  "ORDER BY id DESC LIMIT ?, ?";
        } else {
            sql = "SELECT * FROM student ORDER BY id DESC LIMIT ?, ?";
        }

        try (Connection con = DBConnection.getConnectionToDatabase("college_db");
             PreparedStatement ps = con.prepareStatement(sql)) {

            int offset = (page - 1) * pageSize;

            if (hasKeyword) {
                String kw = "%" + keyword.toLowerCase() + "%";
                ps.setString(1, kw);
                ps.setString(2, kw);
                ps.setString(3, kw);
                ps.setInt(4, offset);
                ps.setInt(5, pageSize);
            } else {
                ps.setInt(1, offset);
                ps.setInt(2, pageSize);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapStudent(rs));
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("Get all failed: " + e.getMessage(), e);
        }

        return list;
    }

    public long count(String keyword) {
        String sql;
        boolean hasKeyword = keyword != null && !keyword.trim().isEmpty();

        if (hasKeyword) {
            sql = "SELECT COUNT(*) FROM student " +
                  "WHERE LOWER(name) LIKE ? OR LOWER(email) LIKE ? OR LOWER(course) LIKE ?";
        } else {
            sql = "SELECT COUNT(*) FROM student";
        }

        try (Connection con = DBConnection.getConnectionToDatabase("college_db");
             PreparedStatement ps = con.prepareStatement(sql)) {

            if (hasKeyword) {
                String kw = "%" + keyword.toLowerCase() + "%";
                ps.setString(1, kw);
                ps.setString(2, kw);
                ps.setString(3, kw);
            }

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("Count failed: " + e.getMessage(), e);
        }

        return 0;
    }

    public long countCourses() {
        String sql = "SELECT COUNT(DISTINCT course) FROM student";

        try (Connection con = DBConnection.getConnectionToDatabase("college_db");
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getLong(1);
            }

        } catch (Exception e) {
            throw new RuntimeException("Count courses failed: " + e.getMessage(), e);
        }

        return 0;
    }

    public Student latest() {
        String sql = "SELECT * FROM student ORDER BY id DESC LIMIT 1";

        try (Connection con = DBConnection.getConnectionToDatabase("college_db");
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return mapStudent(rs);
            }

        } catch (Exception e) {
            throw new RuntimeException("Latest student failed: " + e.getMessage(), e);
        }

        return null;
    }

    private Student mapStudent(ResultSet rs) throws SQLException {
        Student s = new Student();
        s.setId(rs.getInt("id"));
        s.setName(rs.getString("name"));
        s.setEmail(rs.getString("email"));
        s.setCourse(rs.getString("course"));
        s.setPhotoPath(rs.getString("photo_path"));

        Timestamp created = rs.getTimestamp("created_at");
        Timestamp updated = rs.getTimestamp("updated_at");

        if (created != null) {
            s.setCreatedAt(created.toLocalDateTime());
        }
        if (updated != null) {
            s.setUpdatedAt(updated.toLocalDateTime());
        }

        return s;
    }
}