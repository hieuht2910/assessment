package com.digital.order.dto;

import lombok.Data;

@Data
public class QueueRequest {
    private Long shopId;
    private String name;
    private Integer maxSize;
} 