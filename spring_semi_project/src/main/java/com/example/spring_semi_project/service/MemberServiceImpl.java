package com.example.spring_semi_project.service;

import com.example.spring_semi_project.dao.MemberDao;
import com.example.spring_semi_project.dto.*;
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

    @Override
    public void updateRecord(Map map) throws Exception {
        memberDao.updateRecord(map);
    }

    @Override
    public void updateNickname(Map map) throws Exception {
        memberDao.updateNickname(map);
    }

    @Override
    public void updatePassword(Map map) throws Exception {
        memberDao.updatePassword(map);
    }

    @Override
    public void initPassword(Map map) throws Exception {
        memberDao.initPassword(map);
    }

    @Override
    public void saveVerificationCode(VerificationCodes codes) throws Exception {
        memberDao.saveVerificationCode(codes);
    }

    @Override
    public VerificationCodes checkVerificationCode(String userId) throws Exception {
        return memberDao.checkVerificationCode(userId);
    }
}
