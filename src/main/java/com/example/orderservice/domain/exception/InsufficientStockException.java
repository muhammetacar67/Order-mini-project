package com.example.orderservice.domain.exception;

public class InsufficientStockException extends RuntimeException {
    public InsufficientStockException(String productId) {
        super("Yetersiz stok: " + productId);
    }
}