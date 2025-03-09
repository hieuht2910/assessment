package com.digital.order.model;

import jakarta.persistence.*;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@Data
public class Order implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String customerName;
    private String orderDetails;
    private LocalDateTime orderTime;
    
    private Long shopId;
    
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    
    public enum OrderStatus {
        CREATED,
        IN_QUEUE,
        PROCESSING,
        READY,
        CANCELLED
    }
} 