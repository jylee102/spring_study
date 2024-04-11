package com.example.spring_semi_project.service;

import com.example.spring_semi_project.dto.Member;
import com.example.spring_semi_project.dto.Professor;
import com.example.spring_semi_project.dto.Staff;
import com.example.spring_semi_project.dto.Student;

import java.util.Map;

public interface MemberService {
    public Member loginMember(Member member) throws Exception;

    public Staff loginStaff(Map map) throws Exception;

    public Professor loginProfessor(Map map) throws Exception;

    public Student loginStudent(Map map) throws Exception;
}
