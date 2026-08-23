package com.Ajava.StudentcourseEnrollmentSystem.model;

public class Student {

    private Long id;
    private String name;
    private String email;
    private String department;
    private int admissionYear;

    public Student() {
    }

    public Student(Long id, String name, String email,
                   String department, int admissionYear) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.department = department;
        this.admissionYear = admissionYear;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public int getAdmissionYear() {
        return admissionYear;
    }

    public void setAdmissionYear(int admissionYear) {
        this.admissionYear = admissionYear;
    }
}

