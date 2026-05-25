package com.example.orderservice.domain.exception;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(String id) {
        super("Ürün bulunamadı: " + id);
    }
}