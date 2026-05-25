package com.example.orderservice.infrastructure.persistence.adapter;

import com.example.orderservice.domain.model.*;
import com.example.orderservice.domain.port.output.OrderRepositoryPort;
import com.example.orderservice.infrastructure.persistence.entity.*;
import com.example.orderservice.infrastructure.persistence.repository.OrderJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OrderRepositoryAdapter implements OrderRepositoryPort {

    private final OrderJpaRepository orderJpaRepository;

    @Override
    public Order save(Order order) {
        OrderEntity entity = toEntity(order);
        OrderEntity saved = orderJpaRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<Order> findById(String id) {
        return orderJpaRepository.findByIdWithItems(id)
                .map(this::toDomain);
    }

    @Override
    public List<Order> findByCustomerId(String customerId) {
        return orderJpaRepository.findByCustomerId(customerId)
                .stream()
                .map(this::toDomain)
                .toList();
    }

    private OrderEntity toEntity(Order order) {
        OrderEntity orderEntity = OrderEntity.builder()
                .id(order.getId())
                .totalPrice(order.getTotalPrice())
                .status(order.getStatus().name())
                .createdAt(order.getCreatedAt())
                .customer(CustomerEntity.builder()
                        .id(order.getCustomer().getId())
                        .build())
                .build();

        List<OrderItemEntity> items = order.getItems().stream()
                .map(item -> OrderItemEntity.builder()
                        .id(item.getId())
                        .order(orderEntity)
                        .product(ProductEntity.builder()
                                .id(item.getProduct().getId())
                                .build())
                        .quantity(item.getQuantity())
                        .price(item.getPrice())
                        .build())
                .toList();

        orderEntity.setItems(items);
        return orderEntity;
    }

    private Order toDomain(OrderEntity entity) {
        return Order.builder()
                .id(entity.getId())
                .customer(Customer.builder()
                        .id(entity.getCustomer().getId())
                        .name(entity.getCustomer().getName())
                        .build())
                .items(entity.getItems().stream()
                        .map(item -> OrderItem.builder()
                                .id(item.getId())
                                .product(Product.builder()
                                        .id(item.getProduct().getId())
                                        .name(item.getProduct().getName())
                                        .price(item.getProduct().getPrice())
                                        .build())
                                .quantity(item.getQuantity())
                                .price(item.getPrice())
                                .build())
                        .toList())
                .totalPrice(entity.getTotalPrice())
                .status(OrderStatus.valueOf(entity.getStatus()))
                .createdAt(entity.getCreatedAt())
                .build();
    }
}