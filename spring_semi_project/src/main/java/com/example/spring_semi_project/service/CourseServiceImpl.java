package com.example.spring_semi_project.service;

import com.example.spring_semi_project.dao.CourseDao;
import com.example.spring_semi_project.dto.Course;
import com.example.spring_semi_project.dto.Enroll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CourseServiceImpl implements CourseService {
    @Autowired
    CourseDao courseDao;

    @Override
    public List<Course> getCourseList(Map map) throws Exception {
        return courseDao.getCourseList(map);
    }

    @Override
    public int getDataCount(Map map) throws Exception {
        return courseDao.getDataCount(map);
    }

    @Override
    public void updateRestSeat(String courseCode) throws Exception {
        courseDao.updateRestSeat(courseCode);
    }

    @Override
    public void enroll(Enroll enroll) throws Exception {
        courseDao.enroll(enroll);
    }

    @Override
    public List<Course> getMyCourse(Map map) throws Exception {
        return courseDao.getMyCourse(map);
    }

    @Override
    public int getMyDataCount(Map map) throws Exception {
        return courseDao.getMyDataCount(map);
    }

    @Override
    public void deleteCourse(Map map) throws Exception {
        courseDao.deleteCourse(map);
    }

    @Override
    public void restoreRestSeat(String courseCode) throws Exception {
        courseDao.restoreRestSeat(courseCode);
    }
}
