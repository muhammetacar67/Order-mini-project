package com.example.orderservice.domain.port.output;

import com.example.orderservice.domain.model.Product;

import java.util.List;

public interface ProductRepositoryPort {
    List<Product> findByIds(List<String> ids);
    void updateStock(String productId, Integer quantity);
}