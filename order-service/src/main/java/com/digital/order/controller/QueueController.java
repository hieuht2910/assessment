package com.digital.order.controller;

import com.digital.order.model.*;
import com.digital.order.service.QueueService;
import com.digital.order.dto.QueueRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/queues")
@RequiredArgsConstructor
public class QueueController {
    private final QueueService queueService;

    @PostMapping
    public ResponseEntity<Queue> createQueue(@RequestBody QueueRequest request) {
        return ResponseEntity.ok(queueService.createQueue(request));
    }

    @GetMapping("/{queueId}/orders")
    public ResponseEntity<List<QueueOrder>> getQueueOrders(@PathVariable Long queueId) {
        return ResponseEntity.ok(queueService.getQueueOrders(queueId));
    }

    @GetMapping("/{queueId}")
    public ResponseEntity<Queue> getQueueInfo(@PathVariable Long queueId) {
        return ResponseEntity.ok(queueService.getQueueInfo(queueId));
    }

    @GetMapping("/shop/{shopId}")
    public ResponseEntity<List<Queue>> getShopQueues(@PathVariable Long shopId) {
        return ResponseEntity.ok(queueService.getQueuesByShopId(shopId));
    }
} 