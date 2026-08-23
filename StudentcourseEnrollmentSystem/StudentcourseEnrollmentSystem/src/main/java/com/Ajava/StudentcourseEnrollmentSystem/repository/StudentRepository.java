package com.Ajava.StudentcourseEnrollmentSystem.repository;

import com.Ajava.StudentcourseEnrollmentSystem.model.Student;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class StudentRepository {

    private static final List<Student> students = new ArrayList<>();

    static {
        students.add(new Student(1L, "Rahim Ahmed", "rahim@gmail.com", "CSE", 2022));

        students.add(new Student(2L, "Karim Hasan", "karim@gmail.com", "EEE", 2021
        ));

        students.add(new Student(3L, "Nusrat Jahan", "nusrat@gmail.com", "CSE", 2023
        ));

        students.add(new Student(4L, "Sakib Khan", "sakib@gmail.com", "BBA", 2022
        ));

        students.add(new Student(5L, "Mim Akter", "mim@gmail.com", "CSE", 2024
        ));
    }

    public List<Student> findAll() {
        return students;
    }

    public Optional<Student> findById(Long id) {
        return students.stream()
                .filter(student -> student.getId().equals(id))
                .findFirst();
    }

    public Optional<Student> findByEmail(String email) {
        return students.stream()
                .filter(student ->
                        student.getEmail().equalsIgnoreCase(email))
                .findFirst();
    }

    public Student save(Student student) {
        students.add(student);
        return student;
    }

    public void delete(Student student) {
        students.remove(student);
    }


}
