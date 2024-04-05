package com.example.springquiz.service;

import com.example.springquiz.dao.StudentDao;
import com.example.springquiz.dto.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {
    @Autowired
    StudentDao studentDao;

    @Override
    public List<Student> selectList() {
        return studentDao.selectList();
    }
}
