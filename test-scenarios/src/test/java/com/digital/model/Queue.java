package com.digital.model;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Queue {
    private Long id;
    private Long shopId;
    private String name;
    private Integer maxSize;
    private Integer currentSize;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 