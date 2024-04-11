package com.example.spring_semi_project.service;

import com.example.spring_semi_project.dto.Course;

import java.util.List;
import java.util.Map;

public interface CourseService {
    public List<Course> getCourseList(Map map) throws Exception;

    public int getDataCount(Map map) throws Exception;
}
