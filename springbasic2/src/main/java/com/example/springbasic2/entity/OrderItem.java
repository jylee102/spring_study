package com.example.springbasic2.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "order_item")
@Getter
@Setter
public class OrderItem {
    @Id
    @Column(name = "order_item_id")
    private Long orderItemId;

    @Column(name = "order_price")
    private int orderPrice;

    @Column(name = "item_id")
    private Long itemId;

    private int count;

    @Column(name = "order_id")
    private Long orderId;
}
