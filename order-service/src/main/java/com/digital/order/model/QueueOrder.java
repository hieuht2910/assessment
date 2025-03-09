package com.digital.order.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "queue_orders")
@Data
public class QueueOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "queue_id")
    private Queue queue;
    
    @OneToOne
    @JoinColumn(name = "order_id")
    private Order order;
    
    private Integer position;
    private LocalDateTime entryTime;
    
    @PrePersist
    protected void onCreate() {
        entryTime = LocalDateTime.now();
    }
} 