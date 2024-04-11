package com.example.spring_semi_project.dao;

import com.example.spring_semi_project.dto.Member;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MemberDao {
    public Member loginMember(Member member) throws Exception;
}
