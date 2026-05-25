package com.example.orderservice.infrastructure.web.dto;

import lombok.Data;
import java.util.List;

@Data
public class OrderRequest {
    private String customerId;
    private List<String> productIds;
}