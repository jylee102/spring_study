package com.example.spring_semi_project.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Course {
    private String courseCode;
    private String univName;
    private String professorId;
    private String majorCode;
    private String courseName;
    private String classification;
    private int credit;
    private int restSeat;
    private int maxStudent;
    private String classroom;
    private String startTime;
    private String endTime;

    private List<String> courseDay;
    private Subject subject;
    private Professor professor;
    private Enroll enroll;
}
