package com.Ajava.StudentcourseEnrollmentSystem.controller;

import com.Ajava.StudentcourseEnrollmentSystem.model.Student;
import com.Ajava.StudentcourseEnrollmentSystem.service.StudentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }


    @PostMapping
    public ResponseEntity<Student> createStudent(@RequestBody Student student) {

        Student createdStudent = studentService.createStudent(student);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdStudent);
    }


    @GetMapping
    public ResponseEntity<List<Student>> getStudents(@RequestParam(required = false) String department) {

        return ResponseEntity.ok(studentService.getStudents(department)
        );
    }


    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudent(@PathVariable Long id) {

        return ResponseEntity.ok(studentService.getStudent(id)
        );
    }


    @PutMapping("/{id}")
    public ResponseEntity<Student> updateStudent(@PathVariable Long id, @RequestBody Student student) {

        return ResponseEntity.ok(studentService.updateStudent(id, student)
        );
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {studentService.deleteStudent(id);

        return ResponseEntity.noContent().build();
    }
}
