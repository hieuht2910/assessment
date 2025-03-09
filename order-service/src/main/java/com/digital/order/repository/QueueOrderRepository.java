package com.digital.order.repository;

import com.digital.order.model.QueueOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface QueueOrderRepository extends JpaRepository<QueueOrder, Long> {
    List<QueueOrder> findByQueueIdOrderByPositionAsc(Long queueId);
    QueueOrder findByOrderId(Long orderId);
} 