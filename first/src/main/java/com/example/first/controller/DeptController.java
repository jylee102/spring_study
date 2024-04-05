package com.example.first.controller;

//import org.springframework.stereotype.Controller;
import com.example.first.dto.Dept;
import com.example.first.service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

//@Controller
@RestController // 해당 클래스를 컨트롤러로 사용하겠다는 의미
public class DeptController {
    @Autowired
    DeptService deptService; // DeptService deptService = new DeptServiceImpl();

    // @PostMapping("/main") // post 방식으로 요청이 올 때 해당 메소드를 실행
    @GetMapping("/main") // get 방식으로 요청이 올 때 main() 메소드를 실행
    public List<Dept> main() {
        List<Dept> list = deptService.selectList();
        return list; // 리턴해주는 값을 Response body에 담아서 view에 전달
    }
}
