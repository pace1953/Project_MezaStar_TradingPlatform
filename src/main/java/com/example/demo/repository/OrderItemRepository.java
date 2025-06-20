package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.model.entity.OrderItem;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Integer>{
	// ------ 買家 ------
    // 根據訂單查詢明細
    List<OrderItem> findByOrderId(Integer orderId);
    
    // ------ 賣家 ------
 // 根據賣家查詢訂單明細
    @Query("SELECT oi FROM OrderItem oi " +
           "JOIN FETCH oi.card c " +
           "JOIN FETCH c.seller " +
           "JOIN FETCH oi.order o " +
           "JOIN FETCH o.buyer " +
           "WHERE c.sellerId = :sellerId " +
           "ORDER BY o.orderTime DESC")
    List<OrderItem> findBySellerIdOrderByOrderTimeDesc(@Param("sellerId") Integer sellerId);

    // 根據賣家和訂單狀態查詢訂單明細
    @Query("SELECT oi FROM OrderItem oi " +
           "JOIN FETCH oi.card c " +
           "JOIN FETCH c.seller " +
           "JOIN FETCH oi.order o " +
           "JOIN FETCH o.buyer " +
           "WHERE c.sellerId = :sellerId AND o.status = :status " +
           "ORDER BY o.orderTime DESC")
    List<OrderItem> findBySellerIdAndOrderStatusOrderByOrderTimeDesc(
        @Param("sellerId") Integer sellerId, 
        @Param("status") String status);
}
