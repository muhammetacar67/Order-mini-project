package com.example.orderservice.domain.exception;

public class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException(String id) {
        super("Sipariş bulunamadı: " + id);
    }
}