package com.example.spring_semi_project.service;

import com.example.spring_semi_project.dto.Course;
import com.example.spring_semi_project.dto.Enroll;

import java.util.List;
import java.util.Map;

public interface CourseService {
    public List<Course> getCourseList(Map map) throws Exception;

    public int getDataCount(Map map) throws Exception;

    public int countOverlappingCourses(Map map) throws Exception;

    public void updateRestSeat(Enroll enroll) throws Exception;

    public boolean isEnrolled(Enroll enroll) throws Exception;

    public void enroll(Enroll enroll) throws Exception;

    public List<Course> getMyCourse(Map map) throws Exception;

    public int getMyDataCount(Map map) throws Exception;

    public void deleteCourse(Map map) throws Exception;

    public void restoreRestSeat(Map map) throws Exception;

    public Course getCourseInfo(Map map) throws Exception;
}
