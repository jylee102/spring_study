package com.example.spring_semi_project.service;

import com.example.spring_semi_project.dto.*;

import java.util.Map;

public interface MemberService {
    public Member loginMember(Member member) throws Exception;

    public Staff loginStaff(Map map) throws Exception;

    public Professor loginProfessor(Map map) throws Exception;

    public Student loginStudent(Map map) throws Exception;

    public void updateRecord(Map map) throws Exception;

    public void updateNickname(Map map) throws Exception;

    public void updatePassword(Map map) throws Exception;

    public void initPassword(Map map) throws Exception;

    public void saveVerificationCode(VerificationCodes codes) throws Exception;

    public VerificationCodes checkVerificationCode(String userId) throws Exception;
}
