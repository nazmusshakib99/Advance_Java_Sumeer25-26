package com.Ajava.StudentcourseEnrollmentSystem.repository;

import com.Ajava.StudentcourseEnrollmentSystem.model.Enrollment;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class EnrollmentRepository {

    private static final List<Enrollment> enrollments = new ArrayList<>();

    static {
        enrollments.add(new Enrollment(
                1L, 1L, 1L, "Spring 2024", 3.50
        ));

        enrollments.add(new Enrollment(
                2L, 1L, 2L, "Fall 2024", 3.75
        ));

        enrollments.add(new Enrollment(
                3L, 2L, 3L, "Spring 2024", 3.00
        ));

        enrollments.add(new Enrollment(
                4L, 3L, 1L, "Spring 2024", 3.25
        ));

        enrollments.add(new Enrollment(
                5L, 3L, 4L, "Fall 2024", 3.80
        ));

        enrollments.add(new Enrollment(
                6L, 4L, 3L, "Fall 2024", 2.75
        ));

        enrollments.add(new Enrollment(
                7L, 5L, 1L, "Spring 2025", null
        ));

        enrollments.add(new Enrollment(
                8L, 5L, 2L, "Spring 2025", 3.60
        ));
    }

    public List<Enrollment> findAll() {
        return enrollments;
    }

    public boolean existsByStudentId(Long studentId) {
        return enrollments.stream()
                .anyMatch(e -> e.getStudentId().equals(studentId));
    }
}
