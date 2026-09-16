package com.example.eventmanagement.service;

import com.example.eventmanagement.entity.Student;
import com.example.eventmanagement.exception.ApiException;
import com.example.eventmanagement.repository.StudentRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student create(Student student) {
        if (student.getName() == null || student.getName().isBlank()
                || student.getEmail() == null || student.getEmail().isBlank()
                || student.getDepartment() == null || student.getDepartment().isBlank()) {
            throw new ApiException("Name, email and department are required", HttpStatus.BAD_REQUEST);
        }

        if (studentRepository.existsByEmail(student.getEmail())) {
            throw new ApiException("Student email already exists", HttpStatus.CONFLICT);
        }

        return studentRepository.save(student);
    }

    public Student getById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new ApiException("Student not found", HttpStatus.NOT_FOUND));
    }
}
