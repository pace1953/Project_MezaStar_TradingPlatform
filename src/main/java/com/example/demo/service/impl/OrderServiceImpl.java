package com.example.demo.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
@Transactional
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
    @Transactional
    public List<OrderDto> findMyOrders(Integer buyerId) {
    	List<Order> orders = orderRepository.findOrderByBuyerId(buyerId);
        return orders.stream().map(orderMapper::toDto).collect(Collectors.toList());
    }
    
    @Override
    @Transactional
    public Optional<OrderDto> findOrderById(Integer orderId) {
        return orderRepository.findById(orderId)
                .map(orderMapper::toDto);
    }
    
    @Override
    @Transactional
    public Optional<OrderDto> findOrderByNumber(String orderNumber) {
        return orderRepository.findByOrderNumber(orderNumber)
                .map(orderMapper::toDto);
    }
    
    @Override
    @Transactional
    public List<OrderDto> findAllOrders() {
        return orderRepository.findAll().stream()
                .map(orderMapper::toDto)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional
    public List<OrderDto> findOrdersByStatus(String status) {
        return orderRepository.findByStatus(status).stream()
                .map(orderMapper::toDto)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional
    public List<OrderDto> createOrderFromCart(Integer buyerId) throws CartException, CardException {
        
    	// 1. 取得購物車內容
        List<Cart> cartItems = cartRepository.findByBuyerId(buyerId);
        
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
        
        // 3. 根據賣家的ID把購物車的Item分組
        Map<Integer, List<Cart>> cartItemBySeller = cartItems.stream()
        		.collect(Collectors.groupingBy(cart -> cart.getCard().getSellerId()));
        
        // 4. 根據賣家建立訂單
        List<OrderDto> createOrderDtos = new ArrayList<>();
        
        for(Map.Entry<Integer, List<Cart>> entry : cartItemBySeller.entrySet()) {
        	Integer sellerId = entry.getKey();
        	List<Cart> soldCartItem = entry.getValue();
        	
        	int totalAmount = soldCartItem.stream()
        		.mapToInt(cart -> cart.getCard().getPrice() * cart.getQuantity()).sum();
        	int totalItems = soldCartItem.stream()
        		.mapToInt(Cart::getQuantity).sum();
        	
        	// 建立新訂單
        	Order order = new Order();
        	order.setBuyerId(buyerId);
        	order.setSellerId(sellerId);
        	order.setTotalAmount(totalAmount);
        	order.setTotalItems(totalItems);
        	Order saveOrder = orderRepository.save(order);
        	
        	// 建立訂單明細 -> 扣除庫存的數量
        	for(Cart cartItem : soldCartItem) {
        		
        		OrderItem orderItem = orderItemMapper.createFromCart(cartItem, saveOrder.getOrderId());
        		orderItemRepository.save(orderItem);
        		
        		// 扣除庫存數量
        		Card card = cardRepository.findById(cartItem.getCardId()).get();
        		card.updateAvailableQuantity(cartItem.getQuantity());
        		cardRepository.save(card);
        	}
        	
        	// 重新查詢訂單 -> 重新載入關聯
        	Optional<Order> optOrder = orderRepository.findById(saveOrder.getOrderId());
        	if(optOrder.isPresent()) {
        		OrderDto orderDto = orderMapper.toDto(optOrder.get());
        		
        		if(orderDto.getSellerName() == null && !soldCartItem.isEmpty()) {
        			String sellerName = soldCartItem.get(0).getCard().getSeller().getUserName();
        			orderDto.setSellerName(sellerName);
        		}
        		createOrderDtos.add(orderDto);
        	}
        }
        
        // 5. 清空購物車
        cartRepository.deleteByBuyerId(buyerId);
        return createOrderDtos;
    }
    
    @Override
    @Transactional
    public Optional<OrderDto> updateOrderStatus(Integer orderId, String status) {
        return orderRepository.findById(orderId)
                .map(order -> {
                    orderMapper.updateStatus(order, status);
                    Order updatedOrder = orderRepository.save(order);
                    return orderMapper.toDto(updatedOrder);
                });
    }

	@Override
	@Transactional
	public List<OrderDto> findMySoldOrders(Integer sellerId) {
		List<Order> orders = orderRepository.findOrderBySellerId(sellerId);
		return orders.stream()
				.map(order -> orderMapper.toDtoForSeller(order, sellerId))
				// 過濾 -> 過濾不屬於該賣家的訂單(回傳null)
				.filter(orderDto -> orderDto != null)
				.collect(Collectors.toList());
	}

	@Override
	@Transactional
	public List<OrderDto> findSoldOrdersByStatus(Integer sellerId, String status) {
		List<Order> orders = orderRepository.findOrderBySellerIdAndStatus(sellerId, status);
		return 	orders.stream().map(order -> orderMapper.toDtoForSeller(order, sellerId))
				// 過濾 -> 過濾不屬於該賣家的訂單(回傳null)
				.filter(orderDto -> orderDto != null)
				.collect(Collectors.toList());
	}    
}
