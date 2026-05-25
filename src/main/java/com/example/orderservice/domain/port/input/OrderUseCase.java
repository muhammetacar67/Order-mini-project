package com.example.orderservice.domain.port.input;

import com.example.orderservice.domain.model.Order;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface OrderUseCase {
    Order createOrder(String customerId, List<String> productIds);
    Order getOrder(String orderId);
    List<Order> getOrdersByCustomer(String customerId);
    CompletableFuture<Void> processOrderAsync(String orderId);
}