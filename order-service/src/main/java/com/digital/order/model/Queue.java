package com.digital.order.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "queues")
@Data
public class Queue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private Long shopId;
    private String name;
    private Integer maxSize;
    private Integer currentSize;
    
    @Enumerated(EnumType.STRING)
    private QueueStatus status;
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public enum QueueStatus {
        ACTIVE,
        PAUSED,
        CLOSED
    }
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
} 