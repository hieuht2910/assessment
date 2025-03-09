package com.digital.order.dto;

import com.digital.order.model.Order;
import lombok.Data;
import lombok.Builder;

@Data
@Builder
public class OrderStatusResponse {
    private Long orderId;
    private String status;
    private Integer queuePosition;
    private Integer totalCustomersWaiting;
    private Integer estimatedWaitingMinutes;  // Based on average preparation time
} 