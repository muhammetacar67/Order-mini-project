package com.example.orderservice.domain.event;

import com.example.orderservice.domain.model.Order;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class OrderCreatedEvent extends ApplicationEvent {
                                // extends ApplicationEvent kısmı önemli -> spring e ben bir event im diyoruz
    private final Order order;

    public OrderCreatedEvent(Object source, Order order) {
        super(source);
        this.order = order;
    }
}