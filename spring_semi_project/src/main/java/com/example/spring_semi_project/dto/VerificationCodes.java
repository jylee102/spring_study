package com.example.spring_semi_project.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerificationCodes {
    private int id;
    private String userId;
    private String code;
    private String valid;
}
