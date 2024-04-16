package com.example.spring_semi_project.controller;

import com.example.spring_semi_project.dto.Member;
import com.example.spring_semi_project.dto.Student;
import com.example.spring_semi_project.dto.VerificationCodes;
import com.example.spring_semi_project.service.MemberService;
import com.example.spring_semi_project.util.EmailUtil;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Controller
public class MemberController {
    @Autowired
    MemberService memberService;

    @Autowired
    private EmailUtil emailUtil;

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
                session.setAttribute("univName", loginMember.getUniv().getUnivName());
                session.setAttribute("emblem", loginMember.getUniv().getEmblem());
                session.setAttribute("symbolColor", loginMember.getUniv().getSymbolColor());
                session.setAttribute("nickname", loginMember.getNickname());
                session.setAttribute("member_id", loginMember.getId());
                session.setAttribute("role", loginMember.getRole());
                return "redirect:/"; // index 페이지로 이동
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return "redirect:/login?error=true"; // 로그인 실패시 login 페이지로 이동
    }

    @GetMapping(value = "/logout") // localhost/logout
    public String logoutMember(HttpSession session) {
        session.removeAttribute("univName");
        session.removeAttribute("emblem");
        session.removeAttribute("symbolColor");
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

    @PostMapping(value = "/changePw/{pw}")
    public ResponseEntity<String> changePw(@PathVariable("pw") String pw, HttpSession session) {
        try {
            String univName = (String) session.getAttribute("univName");
            String id = (String) session.getAttribute("member_id");

            if (univName == null || id == null) {
                return new ResponseEntity<>("로그인이 필요합니다.", HttpStatus.UNAUTHORIZED);
            }

            Map<String, Object> map = new HashMap<>();

            map.put("univName", univName);
            map.put("id", id);
            map.put("pw", pw);

            memberService.updatePassword(map);
            return new ResponseEntity<String>("비밀번호가 성공적으로 변경되었습니다.", HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("비밀번호 변경에 실패했습니다.", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/initPw")
    public ResponseEntity<String> initPw(HttpSession session) {
        try {
            String univName = (String) session.getAttribute("univName");
            String id = (String) session.getAttribute("member_id");
            String role = (String) session.getAttribute("role");

            if (univName == null || id == null) {
                return new ResponseEntity<>("로그인이 필요합니다.", HttpStatus.UNAUTHORIZED);
            }

            Map<String, Object> map = new HashMap<>();

            map.put("univName", univName);
            map.put("id", id);
            map.put("role", role);

            memberService.initPassword(map);
            return new ResponseEntity<String>("비밀번호가 성공적으로 초기화 되었습니다.", HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("비밀번호 초기화에 실패했습니다.", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/sendVerificationCode/{univName}/{id}")
    public ResponseEntity<String> sendVerificationCode(@PathVariable(value = "univName") String univName,
                                                       @PathVariable(value = "id") String id,
                                                       HttpSession session) {
        String verificationCode = generateRandomCode();
        // 생성된 인증번호를 DB에 저장
        saveVerificationCode(id, verificationCode);

        Map map = new HashMap();
        map.put("univName", univName);
        map.put("id", id);

        session.setAttribute("member_id", id);
        session.setAttribute("univName", univName);

        // 이메일 전송
        try {
            String email = "";
            if (id.length() == 4) {
                email = memberService.loginStaff(map).getEmail();
                session.setAttribute("role", "Staff");
            } else if (id.startsWith("P")) {
                email = memberService.loginProfessor(map).getEmail();
                session.setAttribute("role", "Professor");
            } else if (id.length() == 7) {
                email = memberService.loginStudent(map).getEmail();
                session.setAttribute("role", "Student");
            }

            emailUtil.sendVerificationCode(email, verificationCode);
            return ResponseEntity.ok("인증 코드가 이메일로 전송되었습니다.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("이메일 전송 중 오류가 발생했습니다.");
        }
    }

    private String generateRandomCode() {
        // 랜덤한 6자리 인증 코드 생성 로직
        return String.format("%06d", new Random().nextInt(1000000));
    }

    @PostMapping("/verifyVerificationCode")
    public ResponseEntity<Map<String, Boolean>> verifyVerificationCode(@RequestBody Map<String, String> requestData,
                                                                       HttpSession session) {
        String verificationCode = requestData.get("verificationCode");
        boolean isValid = isValidVerificationCode((String) session.getAttribute("member_id"), verificationCode);

        Map<String, Boolean> response = new HashMap<>();
        response.put("valid", isValid);

        return ResponseEntity.ok(response);
    }

    private void saveVerificationCode(String userId, String verificationCode) {
        try {
            VerificationCodes entity = new VerificationCodes();
            entity.setUserId(userId);
            entity.setCode(verificationCode);
            entity.setValid("N");

            memberService.saveVerificationCode(entity);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private boolean isValidVerificationCode(String userId, String verificationCode) {

        try {
            // DB에서 해당 사용자의 최신 인증번호 조회
            VerificationCodes entity = memberService.checkVerificationCode(userId);

            if (entity != null && entity.getCode().equals(verificationCode) && entity.getValid().equals("N")) {
                // DB에 저장된 인증번호와 사용자 입력값 비교 및 유효성 검사
                return true;
            }

            return false;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
