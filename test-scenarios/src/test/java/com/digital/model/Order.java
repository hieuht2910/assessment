package com.digital.model;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Order {
    private Long id;
    private String customerName;
    private String orderDetails;
    private LocalDateTime orderTime;
    private String status;
} 