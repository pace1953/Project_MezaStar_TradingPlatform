package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import com.example.demo.exception.CardException;
import com.example.demo.exception.CartException;
import com.example.demo.model.dto.OrderDto;

public interface OrderService {
	// ------ 買家 ------
    List<OrderDto> findMyOrders(Integer buyerId);
    Optional<OrderDto> findOrderById(Integer orderId);
    Optional<OrderDto> findOrderByNumber(String orderNumber);
    List<OrderDto> findAllOrders();
    List<OrderDto> findOrdersByStatus(String status);
    
    List<OrderDto> createOrderFromCart(Integer buyerId) throws CartException, CardException;
    Optional<OrderDto> updateOrderStatus(Integer orderId, String status);
    
    // ------ 賣家 ------
    List<OrderDto> findMySoldOrders(Integer sellerId);
    List<OrderDto> findSoldOrdersByStatus(Integer sellerId, String status);
    
    
}
