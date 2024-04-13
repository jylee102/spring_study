package com.example.spring_semi_project.controller;

import com.example.spring_semi_project.dto.Member;
import com.example.spring_semi_project.dto.Student;
import com.example.spring_semi_project.service.MemberService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;

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
                session.setAttribute("univName", loginMember.getUnivName());
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
        session.removeAttribute("univName");
        session.removeAttribute("nickname");
        session.removeAttribute("member_id");
        session.removeAttribute("role");

        return "redirect:/";
    }

    @GetMapping(value = "/myInfo")
    public String myInfo(HttpSession session, Model model) {
        try {
            String univName = (String) session.getAttribute("univName");
            String id = (String) session.getAttribute("member_id");

            if (univName == null || id == null) {
                return "redirect:/login"; // 세션 만료시 로그인 페이지로 이동
            }

            Map map = new HashMap();
            map.put("univName", univName);
            map.put("id", id);

            String role = (String) session.getAttribute("role");

            switch (role) {
                case "Staff" -> model.addAttribute("member", memberService.loginStaff(map));
                case "Professor" -> model.addAttribute("member", memberService.loginProfessor(map));
                case "Student" -> model.addAttribute("member", memberService.loginStudent(map));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return "student/myInfo";
    }

    @PostMapping(value = "/updateRecord")
    public String updateRecord(HttpSession session,
                               @RequestParam("name") String name,
                               @RequestParam("email") String email,
                               @RequestParam("phone") String phone) {
        try {
            String univName = (String) session.getAttribute("univName");
            String id = (String) session.getAttribute("member_id");

            if (univName == null || id == null) {
                return "redirect:/login"; // 세션 만료시 로그인 페이지로 이동
            } else {
                Map<String, Object> map = new HashMap<>();

                map.put("univName", univName);
                map.put("id", id);
                map.put("name", name);
                map.put("email", email);
                map.put("phone", phone);
                map.put("role", session.getAttribute("role"));

                memberService.updateRecord(map);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return "redirect:/myInfo";
    }

    @PostMapping(value = "/updateNickname")
    public String updateNickname(HttpSession session, @RequestParam("nickname") String nickname) {
        try {
            session.setAttribute("nickname", nickname);

            String univName = (String) session.getAttribute("univName");
            String id = (String) session.getAttribute("member_id");

            if (univName == null || id == null) {
                return "redirect:/login"; // 세션 만료시 로그인 페이지로 이동
            } else {
                Map<String, Object> map = new HashMap<>();

                map.put("univName", univName);
                map.put("id", id);
                map.put("nickname", nickname);

                memberService.updateNickname(map);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return "redirect:/myInfo";
    }
}
