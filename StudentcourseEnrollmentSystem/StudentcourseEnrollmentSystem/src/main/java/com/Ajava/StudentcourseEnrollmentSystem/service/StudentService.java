package com.Ajava.StudentcourseEnrollmentSystem.service;

import com.Ajava.StudentcourseEnrollmentSystem.model.Student;
import com.Ajava.StudentcourseEnrollmentSystem.repository.EnrollmentRepository;
import com.Ajava.StudentcourseEnrollmentSystem.repository.StudentRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final EnrollmentRepository enrollmentRepository;

    public StudentService(StudentRepository studentRepository, EnrollmentRepository enrollmentRepository) {
        this.studentRepository = studentRepository;
        this.enrollmentRepository = enrollmentRepository;
    }


    public Student createStudent(Student student) {

        if (studentRepository.findByEmail(student.getEmail()).isPresent()) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Email already exists"
            );
        }

//        student.setId(studentRepository.getNextId());

        return studentRepository.save(student);
    }


    public List<Student> getStudents(String department) {

        if (department == null || department.isBlank()) {
            return studentRepository.findAll();
        }

        return studentRepository.findAll()
                .stream()
                .filter(student ->
                        student.getDepartment()
                                .equalsIgnoreCase(department))
                .toList();
    }


    public Student getStudent(Long id) {

        return studentRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Student not found"
                        ));
    }

    public Student updateStudent(Long id, Student updatedStudent) {

        Student existingStudent = studentRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Student not found"
                        ));


        studentRepository.findByEmail(updatedStudent.getEmail())
                .ifPresent(student -> {
                    if (!student.getId().equals(id)) {
                        throw new ResponseStatusException(HttpStatus.CONFLICT,
                                "Email already exists"
                        );
                    }
                });

        existingStudent.setName(updatedStudent.getName());
        existingStudent.setEmail(updatedStudent.getEmail());
        existingStudent.setDepartment(updatedStudent.getDepartment());
        existingStudent.setAdmissionYear(
                updatedStudent.getAdmissionYear());

        return existingStudent;
    }


    public void deleteStudent(Long id) {

        Student student = studentRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Student not found"
                        ));



    }
}
