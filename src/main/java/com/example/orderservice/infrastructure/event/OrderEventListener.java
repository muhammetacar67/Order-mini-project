package com.example.orderservice.infrastructure.event;

import com.example.orderservice.domain.event.OrderCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OrderEventListener {

    @EventListener
    @Async
    public void handleEmailNotification(OrderCreatedEvent event) {
        log.info("📧 Email gönderiliyor... Order: {}", event.getOrder().getId());
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        log.info("✅ Email gönderildi! Order: {}", event.getOrder().getId());
    }

    @EventListener
    @Async
    public void handleSmsNotification(OrderCreatedEvent event) {
        log.info("📱 SMS gönderiliyor... Order: {}", event.getOrder().getId());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        log.info("✅ SMS gönderildi! Order: {}", event.getOrder().getId());
    }

    @EventListener
    @Async
    public void handleStockUpdate(OrderCreatedEvent event) {
        log.info("📦 Stok güncelleniyor... Order: {}", event.getOrder().getId());
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        log.info("✅ Stok güncellendi! Order: {}", event.getOrder().getId());
    }
}