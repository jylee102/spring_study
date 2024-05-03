package com.expenseTracker.entity;

import com.expenseTracker.constant.Type;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "categories")
@Getter
@Setter
@ToString
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "category_type")
    private Type type; // 주문상태

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    public Category() {};

    public Category(String name, Type type, Member member) {
        this.name = name;
        this.type = type;
        this.member = member;
    }
}
