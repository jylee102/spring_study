package com.example.spring_semi_project.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Member {
    private String univName;
    private String id;
    private String nickname;
    private String password;
    private String role;

    private Univ univ;
}
