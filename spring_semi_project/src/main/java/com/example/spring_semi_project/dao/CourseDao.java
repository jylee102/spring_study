package com.example.spring_semi_project.dao;

import com.example.spring_semi_project.dto.Course;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface CourseDao {
    public List<Course> getCourseList(Map map) throws Exception;

    public int getDataCount(Map map) throws Exception;
}
