package com.example.spring_semi_project.service;

import com.example.spring_semi_project.dao.MemberDao;
import com.example.spring_semi_project.dto.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MemberServiceImpl implements MemberService {
    @Autowired
    private MemberDao memberDao;

    @Override
    public Member loginMember(Member member) throws Exception {
        return memberDao.loginMember(member);
    }
}
