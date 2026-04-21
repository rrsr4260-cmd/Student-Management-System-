package com.example.studentapp.dao;

import com.example.studentapp.model.Marks;
import com.example.studentapp.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class MarksDAO {

    public void save(Marks marks) {
        String sql = "INSERT INTO marks(student_id, subject, marks) VALUES (?, ?, ?)";

        try (Connection con = DBConnection.getConnectionToDatabase("college_db");
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, marks.getStudentId());
            ps.setString(2, marks.getSubject());
            ps.setInt(3, marks.getMarks());
            ps.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException("Failed to save marks: " + e.getMessage(), e);
        }
    }

    public List<Marks> byStudent(int studentId) {
        List<Marks> list = new ArrayList<>();
        String sql = "SELECT * FROM marks WHERE student_id = ? ORDER BY id DESC";

        try (Connection con = DBConnection.getConnectionToDatabase("college_db");
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, studentId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Marks mark = new Marks();
                    mark.setId(rs.getInt("id"));
                    mark.setStudentId(rs.getInt("student_id"));
                    mark.setSubject(rs.getString("subject"));
                    mark.setMarks(rs.getInt("marks"));
                    list.add(mark);
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch marks: " + e.getMessage(), e);
        }

        return list;
    }
}