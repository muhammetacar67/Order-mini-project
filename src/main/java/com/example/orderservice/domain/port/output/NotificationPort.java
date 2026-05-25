package com.example.orderservice.domain.port.output;

import com.example.orderservice.domain.model.Order;

import java.util.concurrent.CompletableFuture;

public interface NotificationPort {
    void sendOrderConfirmation(Order order);

    CompletableFuture<String> sendEmail(String orderId);

    CompletableFuture<String> sendSms(String orderId);

    CompletableFuture<String> updateStock(String orderId);

}