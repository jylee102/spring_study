package com.example.spring_semi_project.service;

import com.example.spring_semi_project.dao.MemberDao;
import com.example.spring_semi_project.dto.Member;
import com.example.spring_semi_project.dto.Professor;
import com.example.spring_semi_project.dto.Staff;
import com.example.spring_semi_project.dto.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class MemberServiceImpl implements MemberService {
    @Autowired
    private MemberDao memberDao;

    @Override
    public Member loginMember(Member member) throws Exception {
        return memberDao.loginMember(member);
    }

    @Override
    public Staff loginStaff(Map map) throws Exception {
        return memberDao.loginStaff(map);
    }

    @Override
    public Professor loginProfessor(Map map) throws Exception {
        return memberDao.loginProfessor(map);
    }

    @Override
    public Student loginStudent(Map map) throws Exception {
        return memberDao.loginStudent(map);
    }
}
