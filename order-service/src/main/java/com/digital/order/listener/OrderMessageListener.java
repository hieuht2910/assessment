package com.digital.order.listener;

import com.digital.order.model.Order;
import com.digital.order.service.OrderService;
import com.digital.order.service.QueueService;
import com.digital.order.config.RabbitMQConfig;
import com.digital.order.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderMessageListener {
    private final OrderService orderService;
    private final QueueService queueService;
    private final OrderRepository orderRepository;

    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    @Transactional
    public void processOrder(Order order) {
        log.info("Received order from queue: {}", order.getId());
        
        try {
            // Update order status to IN_QUEUE
            orderService.updateOrderStatus(order.getId(), Order.OrderStatus.IN_QUEUE);
            log.info("Order {} status updated to IN_QUEUE", order.getId());
            
            // Get the full order with all details
            Order fullOrder = orderRepository.findById(order.getId())
                    .orElseThrow(() -> new RuntimeException("Order not found"));
            
            // Automatically assign to queue based on shop
            try {
                // Use the shopId from the order
                Long shopId = fullOrder.getShopId();
                if (shopId != null) {
                    queueService.assignOrderToQueue(shopId, fullOrder);
                    log.info("Order assigned to queue for shop {}", shopId);
                } else {
                    log.error("Cannot assign order to queue: shopId is null");
                }
            } catch (Exception e) {
                log.error("Failed to assign order to queue", e);
                // Handle exception appropriately
            }
        } catch (Exception e) {
            log.error("Order processing interrupted for order: {}", order.getId(), e);
            Thread.currentThread().interrupt();
        }
    }
} 