package com.example.orderservice.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    private String id;
    private Customer customer;
    private List<OrderItem> items;
    private Double totalPrice;
    private OrderStatus status;
    private LocalDateTime createdAt;
}