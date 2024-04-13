package com.example.spring_semi_project.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Professor {
    private String univName;
    private String id;
    private String majorCode;
    private String name;
    private String office;
    private String phone;
    private String birth;
    private String email;

    private Subject subject;
}
