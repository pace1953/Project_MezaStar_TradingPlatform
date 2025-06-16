package com.example.demo.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.mapper.OrderItemMapper;
import com.example.demo.model.dto.OrderItemDto;
import com.example.demo.repository.OrderItemRepository;
import com.example.demo.service.OrderItemService;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class OrderItemServiceImpl implements OrderItemService {
    
    @Autowired
    private OrderItemRepository orderItemRepository;
    
    @Autowired
    private OrderItemMapper orderItemMapper;
    
    @Override
    @Transactional
    public List<OrderItemDto> findOrderItems(Integer orderId) {
        return orderItemRepository.findByOrderId(orderId).stream()
                .map(orderItemMapper::toDto)
                .collect(Collectors.toList());
    }
}
