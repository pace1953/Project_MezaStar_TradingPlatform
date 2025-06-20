package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.model.entity.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer>{
	
	// ------ 買家 ------
    // 根據訂單號查詢
    Optional<Order> findByOrderNumber(String orderNumber);
    
    // 根據買家查詢訂單
    @Query("SELECT DISTINCT o FROM Order o " +
            "LEFT JOIN FETCH o.buyer " +
            "LEFT JOIN FETCH o.seller " +
            "LEFT JOIN FETCH o.orderItems oi " +
            "LEFT JOIN FETCH oi.card c " +
            "LEFT JOIN FETCH c.seller " +
            "WHERE o.buyerId = :buyerId " +
            "ORDER BY o.orderTime DESC")
     List<Order> findOrderByBuyerId(@Param("buyerId") Integer buyerId);
    
    // 根據狀態查詢訂單
    List<Order> findByStatus(String status);
    
    // ------ 賣家 ------
    // 根據賣家查詢訂單
    @Query("SELECT DISTINCT o FROM Order o " +
            "LEFT JOIN FETCH o.buyer " +
            "LEFT JOIN FETCH o.seller " +
            "LEFT JOIN FETCH o.orderItems oi " +
            "LEFT JOIN FETCH oi.card c " +
            "LEFT JOIN FETCH c.seller " +
            "WHERE o.sellerId = :sellerId " +
            "ORDER BY o.orderTime DESC")
     List<Order> findOrderBySellerId(@Param("sellerId") Integer sellerId);
    
    // 根據賣家和狀態查詢訂單
    @Query("SELECT DISTINCT o FROM Order o " +
            "LEFT JOIN FETCH o.buyer " +
            "LEFT JOIN FETCH o.seller " +
            "LEFT JOIN FETCH o.orderItems oi " +
            "LEFT JOIN FETCH oi.card c " +
            "LEFT JOIN FETCH c.seller " +
            "WHERE o.sellerId = :sellerId AND o.status = :status " +
            "ORDER BY o.orderTime DESC")
     List<Order> findOrderBySellerIdAndStatus(@Param("sellerId") Integer sellerId, 
                                            @Param("status") String status);

    

}
