package com.digital.order.dto;

import lombok.Data;

@Data
public class OrderRequest {
    private String customerName;
    private String orderDetails;
    private Long shopId;  // Only need shop ID, queue assignment will be handled internally
} 