package com.example.springbasic2.entity;

import com.example.springbasic2.constant.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "member")
@Getter
@Setter
public class Member {

    @Id
    @Column(name = "member_id")
    private Long memberId;

    private String email;
    private String name;
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;
}
