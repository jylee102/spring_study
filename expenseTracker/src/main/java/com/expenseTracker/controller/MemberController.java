package com.expenseTracker.controller;

import com.expenseTracker.dto.MemberFormDto;
import com.expenseTracker.entity.Member;
import com.expenseTracker.repository.MemberRepository;
import com.expenseTracker.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class MemberController implements UserDetailsService {
    private final PasswordEncoder passwordEncoder;
    private final MemberService memberService;

    // 문의하기
    @GetMapping(value = "/members/qa") // localhost/members/qa
    public String qa() {
        return "member/qa"; // qa.html
    }

    // 로그인 화면
    @GetMapping(value = "/members/login") // localhost/members/login
    public String loginMember() {
        return "member/memberLoginForm"; // memberLoginForm.html
    }

    // 회원가입 화면
    @GetMapping("/members/new") // localhost/members/new
    public String memberForm(Model model) {
        // 유효성 체크를 위해서 memberFormDto 객체를 매핑하기 위해 전달
        model.addAttribute("memberFormDto", new MemberFormDto());
        return "member/memberForm"; // memberForm.html
    }

    // 회원가입 처리
    @PostMapping(value = "/members/new")
    public String memberForm(@Valid MemberFormDto memberFormDto, BindingResult bindingResult, Model model) {

        // 유효성 검증 에러 발생시 회원가입페이지로 이동시킴
        if (bindingResult.hasErrors()) return "member/memberForm";

        // 유효성 검사를 통과했다면 회원가입 진행
        try {
            Member member = Member.createMember(memberFormDto, passwordEncoder);
            memberService.saveMember(member);
        } catch (IllegalStateException e) {
            // 회원가입이 이미 되어있다면
            model.addAttribute("errorMessage", e.getMessage());
            return "member/memberForm";
        }
        
        return "redirect:/"; // 회원가입 완료 후 메인페이지로 이동
    }

    // 로그인 실패시
    @GetMapping(value = "/members/login/error")
    public String loginError(Model model) {
        model.addAttribute("loginErrorMsg", "아이디 또는 비밀번호를 확인해주세요.");
        return "member/memberLoginForm"; // 로그인 페이지로 그대로 이동
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // 해당 email 계정을 가진 사용자가 있는지 확인
        Member member = memberService.getMember(email);

        if (member == null) { // 사용자가 없다면
            throw new UsernameNotFoundException(email);
        }
        return User.builder()
                .username(member.getEmail())
                .password(member.getPassword())
                .build();
    }
}
