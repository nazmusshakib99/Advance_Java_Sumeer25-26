package com.Ajava.StudentcourseEnrollmentSystem.repository;

import com.Ajava.StudentcourseEnrollmentSystem.model.Course;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class CourseRepository {

    private static final List<Course> courses = new ArrayList<>();

    static {
        courses.add(new Course(1L, "CSE101", "Introduction to Programming", 3.0, "Dr. Rahman", 40
        ));

        courses.add(new Course(2L, "CSE201", "Data Structures", 3.0, "Dr. Karim", 35
        ));

        courses.add(new Course(3L, "EEE101", "Basic Electrical Engineering", 3.0, "Dr. Hasan", 30
        ));

        courses.add(new Course(4L, "CSE301", "Database Systems", 3.0, "Dr. Ahmed", 40
        ));
    }

    public List<Course> findAll() {
        return courses;
    }
}
