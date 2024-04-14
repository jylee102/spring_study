package com.example.spring_semi_project.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CourseTime {
    private String univName;
    private String courseCode;
    private String courseDay;
    private String startTime;
    private String endTime;
}
