package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.entity.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer>{
	
    // 根據訂單號查詢
    Optional<Order> findByOrderNumber(String orderNumber);
    
    // 根據買家查詢訂單
    List<Order> findByBuyerIdOrderByOrderTimeDesc(Integer buyerId);
    
    // 根據狀態查詢訂單
    List<Order> findByStatus(String status);

}
