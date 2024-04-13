package com.example.spring_semi_project.dao;

import com.example.spring_semi_project.dto.Course;
import com.example.spring_semi_project.dto.Enroll;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface CourseDao {
    public List<Course> getCourseList(Map map) throws Exception;

    public int getDataCount(Map map) throws Exception;

    public void updateRestSeat(String courseCode) throws Exception;

    public void enroll(Enroll enroll) throws Exception;

    public List<Course> getMyCourse(Map map) throws Exception;

    public int getMyDataCount(Map map) throws Exception;

    public void deleteCourse(Map map) throws Exception;

    public void restoreRestSeat(String courseCode) throws Exception;
}
