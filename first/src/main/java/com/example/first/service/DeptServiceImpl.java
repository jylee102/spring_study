package com.example.first.service;

import com.example.first.dao.DeptDao;
import com.example.first.dto.Dept;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service // 서비스 역할을 하는 클래스라는 의미
public class DeptServiceImpl implements DeptService {
    @Autowired // DeptDao deptDao = new DeptDaoImpl(); -> 의존성 주입(Dependency Injection)
    DeptDao deptDao;

    @Override
    public List<Dept> selectList() {
        // service 부분은 비즈니스 로직 작성
        return deptDao.selectList(); // List<Dept> 객체 리턴
    }
}
