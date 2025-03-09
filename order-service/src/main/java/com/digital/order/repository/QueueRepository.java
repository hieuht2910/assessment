package com.digital.order.repository;

import com.digital.order.model.Queue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QueueRepository extends JpaRepository<Queue, Long> {
    List<Queue> findByShopId(Long shopId);
} 