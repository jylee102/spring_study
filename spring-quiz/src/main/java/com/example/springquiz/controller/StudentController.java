package com.example.springquiz.controller;

import com.example.springquiz.dto.Student;
import com.example.springquiz.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class StudentController {
    @Autowired
    StudentService studentService;

    @GetMapping("/student")
    public List<Student> student() {
        List<Student> list = studentService.selectList();
        return list;
    }
}
