package com.example.orderservice.infrastructure.notification;

import com.example.orderservice.domain.model.Order;
import com.example.orderservice.domain.port.output.NotificationPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
public class NotificationAdapter implements NotificationPort {

    // 1. Basit @Async kullanımı
    @Async
    @Override
    public void sendOrderConfirmation(Order order) {
        log.info("📧 Bildirim gönderiliyor... Order ID: {}", order.getId());

        // Email gönderme simülasyonu (2 saniye sürsün)
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        log.info("✅ Bildirim gönderildi! Order ID: {}", order.getId());
    }

    // 2. CompletableFuture ile @Async
    @Async
    public CompletableFuture<String> sendOrderConfirmationWithFuture(Order order) {
        log.info("📧 Async future başladı... Order ID: {}", order.getId());

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        log.info("✅ Async future tamamlandı! Order ID: {}", order.getId());
        return CompletableFuture.completedFuture("Bildirim gönderildi: " + order.getId());
    }

    @Async
    public CompletableFuture<String> sendEmail(String orderId) {
        log.info("📧 Email gönderiliyor... Order: {}", orderId);
        try {
            Thread.sleep(2000); // 2 sn
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        log.info("✅ Email gönderildi! Order: {}", orderId);
        return CompletableFuture.completedFuture("Email OK");
    }

    @Async
    public CompletableFuture<String> sendSms(String orderId) {
        log.info("📱 SMS gönderiliyor... Order: {}", orderId);
        try {
            Thread.sleep(1000); // 1 sn
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        log.info("✅ SMS gönderildi! Order: {}", orderId);
        return CompletableFuture.completedFuture("SMS OK");
    }

    @Async
    public CompletableFuture<String> updateStock(String orderId) {
        log.info("📦 Stok güncelleniyor... Order: {}", orderId);
        try {
            Thread.sleep(1500); // 1.5 sn
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        log.info("✅ Stok güncellendi! Order: {}", orderId);
        return CompletableFuture.completedFuture("Stock OK");
    }
}