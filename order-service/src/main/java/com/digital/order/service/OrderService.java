package com.digital.order.service;

import com.digital.order.model.Order;
import com.digital.order.repository.OrderRepository;
import com.digital.order.dto.OrderRequest;
import com.digital.order.config.RabbitMQConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {
    private final OrderRepository orderRepository;
    private final RabbitTemplate rabbitTemplate;
    private final SimpMessagingTemplate messagingTemplate;
    private final QueueService queueService;

    @Transactional
    public Order createOrder(OrderRequest orderRequest) {
        log.info("Creating new order for customer: {}", orderRequest.getCustomerName());
        Order order = new Order();
        order.setCustomerName(orderRequest.getCustomerName());
        order.setOrderDetails(orderRequest.getOrderDetails());
        order.setOrderTime(LocalDateTime.now());
        order.setStatus(Order.OrderStatus.CREATED);
        order.setShopId(orderRequest.getShopId());
        
        Order savedOrder = orderRepository.save(order);
        log.info("Order saved with ID: {}", savedOrder.getId());

        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, 
                                    RabbitMQConfig.ROUTING_KEY, 
                                    savedOrder);
        
        return savedOrder;
    }

    public Order updateOrderStatus(Long orderId, Order.OrderStatus status) {
        log.info("Updating order {} status to: {}", orderId, status);
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        
        order.setStatus(status);
        Order updatedOrder = orderRepository.save(order);
        log.info("Order {} status updated successfully", orderId);
        
        messagingTemplate.convertAndSend("/topic/order." + orderId, updatedOrder);
        log.info("WebSocket notification sent for order: {}", orderId);
        
        return updatedOrder;
    }

    public void cancelOrder(Long orderId) {
        log.info("Cancelling order: {}", orderId);
        updateOrderStatus(orderId, Order.OrderStatus.CANCELLED);
        log.info("Order {} cancelled successfully", orderId);
    }
} 