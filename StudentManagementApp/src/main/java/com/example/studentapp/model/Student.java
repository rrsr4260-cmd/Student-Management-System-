package com.example.studentapp.model;

import java.time.LocalDateTime;

public class Student {

    private int id;
    private String name;
    private String email;
    private String course;
    private String photoPath;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Default Constructor
    public Student() {}

    // Parameterized Constructor
    public Student(String name, String email, String course, String photoPath) {
        this.name = name;
        this.email = email;
        this.course = course;
        this.photoPath = photoPath;
    }

    // Full Constructor
    public Student(int id, String name, String email, String course, String photoPath,
                   LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.course = course;
        this.photoPath = photoPath;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // ================= GETTERS =================

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getCourse() {
        return course;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    // ================= SETTERS =================

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    // ================= toString =================

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", course='" + course + '\'' +
                ", photoPath='" + photoPath + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}