package com.digital.model;

import lombok.Data;

@Data
public class OrderRequest {
    private String customerName;
    private String orderDetails;
    private Long shopId;
} 