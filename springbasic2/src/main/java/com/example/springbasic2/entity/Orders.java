package com.example.springbasic2.entity;

import com.example.springbasic2.constant.OrderStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@Getter
@Setter
public class Orders {
    @Id
    @Column(name = "order_id")
    private Long orderId;

    @Column(name = "order_date", length = 6)
    private LocalDateTime orderDate;

    @Column(name = "order_status")
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @Column(name = "member_id")
    private Long memberId;
}
