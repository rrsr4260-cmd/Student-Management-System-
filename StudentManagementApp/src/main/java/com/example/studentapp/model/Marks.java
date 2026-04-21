package com.example.studentapp.model;

public class Marks {

    private int id;
    private int studentId;
    private String subject;
    private int marks;

    public Marks() {
    }

    public Marks(int studentId, String subject, int marks) {
        this.studentId = studentId;
        this.subject = subject;
        this.marks = marks;
    }

    public int getId() {
        return id;
    }

    public int getStudentId() {
        return studentId;
    }

    public String getSubject() {
        return subject;
    }

    public int getMarks() {
        return marks;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setMarks(int marks) {
        this.marks = marks;
    }
}