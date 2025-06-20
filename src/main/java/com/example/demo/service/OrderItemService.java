package com.example.demo.service;

import java.util.List;

import com.example.demo.model.dto.OrderItemDto;

public interface OrderItemService {
    
	// ------ 買家 ------
    List<OrderItemDto> findOrderItems(Integer orderId);
    
    // ------ 賣家 ------
    List<OrderItemDto> findSoldOrderItems(Integer sellerId);
    List<OrderItemDto> findSoldOrderByStatus(Integer sellerId, String status); 
}
