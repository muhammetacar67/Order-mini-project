package com.example.orderservice.infrastructure.web.controller;

import com.example.orderservice.domain.model.Order;
import com.example.orderservice.domain.port.input.OrderUseCase;
import com.example.orderservice.infrastructure.web.dto.OrderItemResponse;
import com.example.orderservice.infrastructure.web.dto.OrderRequest;
import com.example.orderservice.infrastructure.web.dto.OrderResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
     // OrderUseCase interface olduğunu biliyorsun ya şimdi şöyle bir durum var spring burada ne yapıyor aslında gidip bu interface i implement eden concrete classları buraya inject ediyor
    private final OrderUseCase orderUseCase;

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@RequestBody OrderRequest request) {
        Order order = orderUseCase.createOrder(
                request.getCustomerId(),
                request.getProductIds()
        );
        return ResponseEntity.ok(toResponse(order));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrder(@PathVariable String orderId) {
        Order order = orderUseCase.getOrder(orderId);
        return ResponseEntity.ok(toResponse(order));
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<OrderResponse>> getOrdersByCustomer(
            @PathVariable String customerId) {
        return ResponseEntity.ok(
                orderUseCase.getOrdersByCustomer(customerId)
                        .stream()
                        .map(this::toResponse)
                        .toList()
        );
    }

    @PostMapping("/{orderId}/process")
    public ResponseEntity<String> processOrder(@PathVariable String orderId) {
        orderUseCase.processOrderAsync(orderId);
        return ResponseEntity.ok("İşlemler başlatıldı!");
    }

    private OrderResponse toResponse(Order order) {
        return OrderResponse.builder()
                .id(order.getId())
                .customerId(order.getCustomer().getId())
                .items(order.getItems().stream()
                        .map(item -> OrderItemResponse.builder()
                                .productId(item.getProduct().getId())
                                .productName(item.getProduct().getName())
                                .quantity(item.getQuantity())
                                .price(item.getPrice())
                                .build())
                        .toList())
                .totalPrice(order.getTotalPrice())
                .status(order.getStatus().name())
                .createdAt(order.getCreatedAt())
                .build();
    }
}