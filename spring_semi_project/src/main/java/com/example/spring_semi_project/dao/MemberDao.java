package com.example.spring_semi_project.dao;

import com.example.spring_semi_project.dto.Member;
import com.example.spring_semi_project.dto.Professor;
import com.example.spring_semi_project.dto.Staff;
import com.example.spring_semi_project.dto.Student;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

@Mapper
public interface MemberDao {
    public Member loginMember(Member member) throws Exception;

    public Staff loginStaff(Map map) throws Exception;

    public Professor loginProfessor(Map map) throws Exception;

    public Student loginStudent(Map map) throws Exception;
}
