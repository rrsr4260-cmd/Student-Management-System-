package com.example.studentapp.model;

import java.time.LocalDate;

public class Attendance {

    private int id;
    private int studentId;
    private LocalDate date;
    private String status;

    public Attendance() {
    }

    public Attendance(int studentId, LocalDate date, String status) {
        this.studentId = studentId;
        this.date = date;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public int getStudentId() {
        return studentId;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getStatus() {
        return status;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}