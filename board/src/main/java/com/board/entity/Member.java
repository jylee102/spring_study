package com.board.entity;

import com.board.dto.MemberFormDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.crypto.password.PasswordEncoder;

@Entity
@Table(name = "member")
@Getter
@Setter
@ToString
public class Member {
    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;

    private String name;

    private String password;

    // MemberFormDto -> Member 엔티티 객체로 변환
    // JPA에서는 영속성 컨텍스트에 엔티티 객체를 통해 CRUD를 진행하므로 DTO 객체를 반드시 엔티티 객체로 변경해줘야 한다.
    public static Member createMember(MemberFormDto memberFormDto, PasswordEncoder passwordEncoder) {
        // 패스워드 암호화
        String password = passwordEncoder.encode(memberFormDto.getPassword());

        Member member = new Member();

        // 사용자가 입력한 회원가입 정보를 member 엔티티로 변환
        member.setName(memberFormDto.getName());
        member.setEmail(memberFormDto.getEmail());
        member.setPassword(password);

        return member;
    }
}
