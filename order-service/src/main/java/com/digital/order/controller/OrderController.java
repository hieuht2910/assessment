package com.digital.order.controller;

import com.digital.order.dto.OrderRequest;
import com.digital.order.dto.OrderStatusResponse;
import com.digital.order.model.Order;
import com.digital.order.service.OrderService;
import com.digital.order.service.QueueService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final QueueService queueService;

    @PostMapping
    public ResponseEntity<OrderStatusResponse> createOrder(@RequestBody OrderRequest orderRequest) {
        Order order = orderService.createOrder(orderRequest);
        return ResponseEntity.ok(queueService.getOrderStatus(order.getId()));
    }

    @GetMapping("/{orderId}/status")
    public ResponseEntity<OrderStatusResponse> getOrderStatus(@PathVariable Long orderId) {
        return ResponseEntity.ok(queueService.getOrderStatus(orderId));
    }

    @PutMapping("/{orderId}/cancel")
    public ResponseEntity<Void> cancelOrder(@PathVariable Long orderId) {
        orderService.cancelOrder(orderId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{orderId}/status")
    public ResponseEntity<Order> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestParam Order.OrderStatus status) {
        return ResponseEntity.ok(orderService.updateOrderStatus(orderId, status));
    }
} 