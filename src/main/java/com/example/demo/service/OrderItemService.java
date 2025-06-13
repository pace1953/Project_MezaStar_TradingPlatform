package com.example.demo.service;

import java.util.List;

import com.example.demo.model.dto.OrderItemDto;

public interface OrderItemService {
    
    List<OrderItemDto> findOrderItems(Integer orderId);
}
