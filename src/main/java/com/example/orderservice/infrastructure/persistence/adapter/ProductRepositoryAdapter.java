package com.example.orderservice.infrastructure.persistence.adapter;

import com.example.orderservice.domain.model.Product;
import com.example.orderservice.domain.port.output.ProductRepositoryPort;
import com.example.orderservice.infrastructure.persistence.entity.ProductEntity;
import com.example.orderservice.infrastructure.persistence.repository.ProductJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ProductRepositoryAdapter implements ProductRepositoryPort {

    private final ProductJpaRepository productJpaRepository;

    @Override
    public List<Product> findByIds(List<String> ids) {
        return productJpaRepository.findByIdIn(ids)
                .stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public void updateStock(String productId, Integer quantity) {
        productJpaRepository.findById(productId).ifPresent(product -> {
            ProductEntity updated = ProductEntity.builder()
                    .id(product.getId())
                    .name(product.getName())
                    .price(product.getPrice())
                    .stock(product.getStock() - quantity)
                    .build();
            productJpaRepository.save(updated);
        });
    }

    private Product toDomain(ProductEntity entity) {
        return Product.builder()
                .id(entity.getId())
                .name(entity.getName())
                .price(entity.getPrice())
                .stock(entity.getStock())
                .build();
    }
}