package com.digital.order.service;

import com.digital.order.model.*;
import com.digital.order.repository.*;
import com.digital.order.dto.OrderStatusResponse;
import com.digital.order.dto.QueueRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class QueueService {
    private final QueueRepository queueRepository;
    private final QueueOrderRepository queueOrderRepository;
    
    private static final int AVERAGE_PREPARATION_MINUTES = 5; // Average time per order

    @Transactional
    public QueueOrder addOrderToQueue(Long queueId, Order order) {
        Queue queue = queueRepository.findById(queueId)
                .orElseThrow(() -> new RuntimeException("Queue not found"));
        
        if (queue.getCurrentSize() >= queue.getMaxSize()) {
            throw new RuntimeException("Queue is full");
        }

        // Get current position
        List<QueueOrder> queueOrders = queueOrderRepository.findByQueueIdOrderByPositionAsc(queueId);
        int position = queueOrders.isEmpty() ? 1 : queueOrders.get(queueOrders.size() - 1).getPosition() + 1;

        QueueOrder queueOrder = new QueueOrder();
        queueOrder.setQueue(queue);
        queueOrder.setOrder(order);
        queueOrder.setPosition(position);

        queue.setCurrentSize(queue.getCurrentSize() + 1);
        queueRepository.save(queue);

        return queueOrderRepository.save(queueOrder);
    }

    @Transactional
    public void removeOrderFromQueue(Long orderId) {
        QueueOrder queueOrder = queueOrderRepository.findByOrderId(orderId);
        if (queueOrder == null) {
            return;
        }

        Queue queue = queueOrder.getQueue();
        queue.setCurrentSize(queue.getCurrentSize() - 1);
        queueRepository.save(queue);

        // Reorder positions for remaining orders
        List<QueueOrder> remainingOrders = queueOrderRepository.findByQueueIdOrderByPositionAsc(queue.getId());
        for (int i = 0; i < remainingOrders.size(); i++) {
            remainingOrders.get(i).setPosition(i + 1);
        }
        queueOrderRepository.saveAll(remainingOrders);

        queueOrderRepository.delete(queueOrder);
    }

    public List<QueueOrder> getQueueOrders(Long queueId) {
        return queueOrderRepository.findByQueueIdOrderByPositionAsc(queueId);
    }

    public Queue getQueueInfo(Long queueId) {
        return queueRepository.findById(queueId)
                .orElseThrow(() -> new RuntimeException("Queue not found"));
    }

    @Transactional
    public QueueOrder assignOrderToQueue(Long shopId, Order order) {
        log.info("Assigning order {} to shop {}", order.getId(), shopId);
        
        // Find the shortest queue for the shop
        List<Queue> shopQueues = queueRepository.findByShopId(shopId);
        if (shopQueues.isEmpty()) {
            throw new RuntimeException("No queues available for shop " + shopId);
        }

        Queue selectedQueue = shopQueues.stream()
                .filter(q -> q.getStatus() == Queue.QueueStatus.ACTIVE)
                .min((q1, q2) -> q1.getCurrentSize().compareTo(q2.getCurrentSize()))
                .orElseThrow(() -> new RuntimeException("No active queues available"));

        return addOrderToQueue(selectedQueue.getId(), order);
    }

    public OrderStatusResponse getOrderStatus(Long orderId) {
        QueueOrder queueOrder = queueOrderRepository.findByOrderId(orderId);
        if (queueOrder == null) {
            return OrderStatusResponse.builder()
                    .orderId(orderId)
                    .status("NOT_IN_QUEUE")
                    .build();
        }

        Queue queue = queueOrder.getQueue();
        int position = queueOrder.getPosition();
        
        return OrderStatusResponse.builder()
                .orderId(orderId)
                .status(queueOrder.getOrder().getStatus().toString())
                .queuePosition(position)
                .totalCustomersWaiting(queue.getCurrentSize())
                .estimatedWaitingMinutes(position * AVERAGE_PREPARATION_MINUTES)
                .build();
    }

    @Transactional
    public Queue createQueue(QueueRequest request) {
        log.info("Creating new queue for shop: {}", request.getShopId());
        
        Queue queue = new Queue();
        queue.setShopId(request.getShopId());
        queue.setName(request.getName());
        queue.setMaxSize(request.getMaxSize());
        queue.setCurrentSize(0);
        queue.setStatus(Queue.QueueStatus.ACTIVE);
        
        Queue savedQueue = queueRepository.save(queue);
        log.info("Created queue with ID: {} for shop: {}", savedQueue.getId(), savedQueue.getShopId());
        
        return savedQueue;
    }

    public List<Queue> getQueuesByShopId(Long shopId) {
        return queueRepository.findByShopId(shopId);
    }
} 