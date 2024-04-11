package com.example.spring_semi_project.controller;

import com.example.spring_semi_project.dto.Member;
import com.example.spring_semi_project.service.MemberService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class MemberController {
    @Autowired
    MemberService memberService;

    @GetMapping(value = "/login") // localhost/login
    public String login() {
        return "member/login";
    }

    @PostMapping(value = "/login")
    public String loginMember(Member member, HttpSession session) { // 로그인 처리
        // 사용자가 입력한 로그인 데이터와 DB에 저장된 데이터가 일치하는지 비교
        try {
            Member loginMember = memberService.loginMember(member);

            if (loginMember != null) { // 데이터가 일치하면(로그인 성공시)
                // 로그인 성공시 로그인한 사람의 name과 member_id를 세션에 저장
                // .setAttribute(키, 값) -> 세션에 값을 저장
                session.setAttribute("univ", loginMember.getUnivName());
                session.setAttribute("nickname", loginMember.getNickname());
                session.setAttribute("member_id", loginMember.getId());
                session.setAttribute("role", loginMember.getRole());
                return "redirect:/"; // index 페이지로 이동
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return "member/login"; // 로그인 실패시 login 페이지로 이동
    }

    @GetMapping(value = "/logout") // localhost/logout
    public String logoutMember(HttpSession session) {
        session.removeAttribute("univ");
        session.removeAttribute("nickname");
        session.removeAttribute("member_id");
        session.removeAttribute("role");

        return "redirect:/";
    }
}
