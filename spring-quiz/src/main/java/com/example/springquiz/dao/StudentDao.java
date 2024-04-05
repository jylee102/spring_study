package com.example.springquiz.dao;

import com.example.springquiz.dto.Student;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface StudentDao {
    public List<Student> selectList();
}
