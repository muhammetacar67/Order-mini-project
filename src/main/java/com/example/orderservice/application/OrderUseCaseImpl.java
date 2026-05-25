package com.example.orderservice.application;

import com.example.orderservice.domain.exception.OrderNotFoundException;
import com.example.orderservice.domain.exception.ProductNotFoundException;
import com.example.orderservice.domain.model.*;
import com.example.orderservice.domain.port.input.OrderUseCase;
import com.example.orderservice.domain.port.output.NotificationPort;
import com.example.orderservice.domain.port.output.OrderRepositoryPort;
import com.example.orderservice.domain.port.output.ProductRepositoryPort;
import com.example.orderservice.infrastructure.notification.NotificationAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderUseCaseImpl implements OrderUseCase {

    private final OrderRepositoryPort orderRepositoryPort;
    private final ProductRepositoryPort productRepositoryPort;
    private final NotificationPort notificationPort;

    @Override
    public Order createOrder(String customerId, List<String> productIds) {

        // 1. Ürünleri getir
        List<Product> products = productRepositoryPort.findByIds(productIds);

        // Ürün bulunamadıysa hata fırlat
        if (products.isEmpty()) {
            throw new ProductNotFoundException(productIds.toString());
        }


        // 2. Sipariş itemlarını oluştur
        List<OrderItem> items = products.stream()
                .map(product -> OrderItem.builder()
                        .id(UUID.randomUUID().toString())
                        .product(product)
                        .quantity(1)
                        .price(product.getPrice())
                        .build())
                .toList();

        // 3. Toplam fiyatı hesapla
        Double totalPrice = items.stream()
                .mapToDouble(OrderItem::getPrice)
                .sum();

        // 4. Siparişi oluştur
        Order order = Order.builder()
                .id(UUID.randomUUID().toString())
                .customer(Customer.builder().id(customerId).build())
                .items(items)
                .totalPrice(totalPrice)
                .status(OrderStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();

        // 5. Kaydet
        Order savedOrder = orderRepositoryPort.save(order);

        // 6. Bildirim gönder (Async olacak)
        notificationPort.sendOrderConfirmation(savedOrder);

        return savedOrder;
    }

    @Override
    public Order getOrder(String orderId) {
        return orderRepositoryPort.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
    }

    @Override
    public List<Order> getOrdersByCustomer(String customerId) {
        return orderRepositoryPort.findByCustomerId(customerId);
    }


    @Override
    public CompletableFuture<Void> processOrderAsync(String orderId) {
        log.info("🚀 Paralel işlemler başlıyor... Order: {}", orderId);

        CompletableFuture<String> emailFuture = notificationPort.sendEmail(orderId);
        CompletableFuture<String> smsFuture = notificationPort.sendSms(orderId);
        CompletableFuture<String> stockFuture = notificationPort.updateStock(orderId);

        return CompletableFuture.allOf(emailFuture, smsFuture, stockFuture)
                .thenRun(() -> log.info("🎉 Tüm işlemler tamamlandı! Order: {}", orderId));
    }
}