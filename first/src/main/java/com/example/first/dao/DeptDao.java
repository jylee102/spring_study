package com.example.first.dao;

import com.example.first.dto.Dept;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper // Mybatis mapper 파일과 연동하기 위해 붙여야 한다.
public interface DeptDao {
    // mapper 파일에 작성한 쿼리문을 호출하기 위해 사용
    public List<Dept> selectList();
}
