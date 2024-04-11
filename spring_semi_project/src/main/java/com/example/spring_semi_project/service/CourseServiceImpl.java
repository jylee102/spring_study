package com.example.spring_semi_project.service;

import com.example.spring_semi_project.dao.CourseDao;
import com.example.spring_semi_project.dto.Course;
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
}
