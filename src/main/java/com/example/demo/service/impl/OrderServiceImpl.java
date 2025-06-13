package com.example.demo.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.mapper.OrderMapper;
import com.example.demo.exception.CardException;
import com.example.demo.exception.CartException;
import com.example.demo.mapper.OrderItemMapper;
import com.example.demo.model.dto.OrderDto;
import com.example.demo.model.entity.Order;
import com.example.demo.model.entity.OrderItem;
import com.example.demo.model.entity.Card;
import com.example.demo.model.entity.Cart;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.OrderItemRepository;
import com.example.demo.repository.CardRepository;
import com.example.demo.repository.CartRepository;
import com.example.demo.service.OrderService;

@Service
public class OrderServiceImpl implements OrderService {
    
    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private OrderItemRepository orderItemRepository;
    
    @Autowired
    private CartRepository cartRepository;
    
    @Autowired
    private CardRepository cardRepository;
    
    @Autowired
    private OrderMapper orderMapper;
    
    @Autowired
    private OrderItemMapper orderItemMapper;
    
    @Override
    public List<OrderDto> findMyOrders(Integer buyerId) {
        return orderRepository.findByBuyerIdOrderByOrderTimeDesc(buyerId).stream()
                .map(orderMapper::toDto)
                .collect(Collectors.toList());
    }
    
    @Override
    public Optional<OrderDto> findOrderById(Integer orderId) {
        return orderRepository.findById(orderId)
                .map(orderMapper::toDto);
    }
    
    @Override
    public Optional<OrderDto> findOrderByNumber(String orderNumber) {
        return orderRepository.findByOrderNumber(orderNumber)
                .map(orderMapper::toDto);
    }
    
    @Override
    public List<OrderDto> findAllOrders() {
        return orderRepository.findAll().stream()
                .map(orderMapper::toDto)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<OrderDto> findOrdersByStatus(String status) {
        return orderRepository.findByStatus(status).stream()
                .map(orderMapper::toDto)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional
    public OrderDto createOrderFromCart(Integer buyerId) throws CartException, CardException {
        // 1. 取得購物車內容
        List<Cart> cartItems = cartRepository.findByBuyerIdWithDetails(buyerId);
        
        if (cartItems.isEmpty()) {
            throw new CartException("購物車是空的");
        }
        
        // 2. 檢查所有卡匣的庫存
        for(Cart cartItem : cartItems) {
        	Optional<Card> optCard = cardRepository.findById(cartItem.getCardId());
        	if(optCard.isEmpty()) {
        		throw new CardException("卡匣不存在:"+cartItem.getCardId());
        	}
        	
        	Card card = optCard.get();
        	if(!"上架中".equals(card.getStatus())) {
        		throw new CartException("卡匣已下架:"+card.getCardName());
        	}
        	
        	if(card.getAvailableQuantity() < cartItem.getQuantity()) {
        		throw new CartException("庫存不足:"+card.getQuantity()+
        				"，需要:"+cartItem.getQuantity()+"，目前可用:"+card.getAvailableQuantity());
        	}
        	
        }
        
        // 3. 計算總金額和總數量
        int totalAmount = cartItems.stream()
                .mapToInt(cart -> cart.getCard().getPrice() * cart.getQuantity())
                .sum();
        
        int totalItems = cartItems.stream()
                .mapToInt(Cart::getQuantity)
                .sum();
        
        // 4. 建立訂單
        Order order = new Order();
        order.setBuyerId(buyerId);
        order.setTotalAmount(totalAmount);
        order.setTotalItems(totalItems);
        Order savedOrder = orderRepository.save(order);
        
        // 5. 建立訂單明細並扣除庫存數量
        for (Cart cartItem : cartItems) {
        	// 建立訂單明細
            OrderItem orderItem = orderItemMapper.createFromCart(cartItem, savedOrder.getOrderId());
            orderItemRepository.save(orderItem);
            
            // 扣除庫存數量
            Card card = cardRepository.findById(cartItem.getCardId()).get();
            card.updateAvailableQuantity(cartItem.getQuantity());
            cardRepository.save(card);
        }
        
        // 6. 清空購物車
        cartRepository.deleteByBuyerId(buyerId);
        
        return orderMapper.toDto(savedOrder);
    }
    
    @Override
    public Optional<OrderDto> updateOrderStatus(Integer orderId, String status) {
        return orderRepository.findById(orderId)
                .map(order -> {
                    orderMapper.updateStatus(order, status);
                    Order updatedOrder = orderRepository.save(order);
                    return orderMapper.toDto(updatedOrder);
                });
    }
}
