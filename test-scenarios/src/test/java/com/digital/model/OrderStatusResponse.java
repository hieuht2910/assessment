package com.digital.model;

import lombok.Data;

@Data
public class OrderStatusResponse {
    private Long orderId;
    private String status;
    private Integer queuePosition;
    private Integer totalCustomersWaiting;
    private Integer estimatedWaitingMinutes;
} 