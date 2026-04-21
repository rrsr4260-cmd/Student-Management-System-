package com.example.studentapp.dao;

import com.example.studentapp.model.Attendance;
import com.example.studentapp.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class AttendanceDAO {

    public void save(Attendance attendance) {
        String sql = "INSERT INTO attendance(student_id, date, status) VALUES (?, ?, ?)";

        try (Connection con = DBConnection.getConnectionToDatabase("college_db");
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, attendance.getStudentId());
            ps.setDate(2, java.sql.Date.valueOf(attendance.getDate()));
            ps.setString(3, attendance.getStatus());
            ps.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException("Failed to save attendance: " + e.getMessage(), e);
        }
    }

    public List<Attendance> byStudent(int studentId) {
        List<Attendance> list = new ArrayList<>();
        String sql = "SELECT * FROM attendance WHERE student_id = ? ORDER BY date DESC";

        try (Connection con = DBConnection.getConnectionToDatabase("college_db");
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, studentId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Attendance attendance = new Attendance();
                    attendance.setId(rs.getInt("id"));
                    attendance.setStudentId(rs.getInt("student_id"));
                    attendance.setDate(rs.getDate("date").toLocalDate());
                    attendance.setStatus(rs.getString("status"));
                    list.add(attendance);
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch attendance: " + e.getMessage(), e);
        }

        return list;
    }
}