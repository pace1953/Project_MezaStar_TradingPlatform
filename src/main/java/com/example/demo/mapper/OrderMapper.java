package com.example.demo.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.demo.model.dto.OrderDto;
import com.example.demo.model.dto.OrderItemDto;
import com.example.demo.model.entity.Order;

@Component
public class OrderMapper {

	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private OrderItemMapper orderItemMapper;
	
	// Entity 轉 DTO
	public OrderDto toDto(Order order) {
		OrderDto orderDto = modelMapper.map(order, OrderDto.class);
		
		orderDto.setOrderTime(order.getOrderTime());
		orderDto.setCompletedTime(order.getCompletedTime());
		
		// 如果 buyer 關聯已載入 -> 設定買家名稱
		if (order.getBuyer() != null) {
			orderDto.setBuyerName(order.getBuyer().getUserName());
		}
		
		// 如果 seller 關聯已載入 -> 設定賣家名稱 (從Order抓)
		orderDto.setSellerId(order.getSellerId());
		if(order.getSeller() != null) {
			orderDto.setSellerName(order.getSeller().getUserName());
		}
		
		// 如果 orderItems 關聯已載入 -> 轉換訂單明細
		if (order.getOrderItems() != null && !order.getOrderItems().isEmpty()) {
			List<OrderItemDto> orderItemDtos = order.getOrderItems().stream()
					.map(orderItemMapper::toDto)
					.collect(Collectors.toList());
			orderDto.setOrderItems(orderItemDtos);
			
			// 卡匣的資訊 -> 訂單 (要給前端顯示卡匣的資訊)
			if(!orderItemDtos.isEmpty()) {
				OrderItemDto fitstItemDtos = orderItemDtos.get(0);
				orderDto.setCardName(fitstItemDtos.getCardName());
				orderDto.setSeries(fitstItemDtos.getSeries());
				orderDto.setStarLevel(fitstItemDtos.getStarLevel());
			}
		}
		return orderDto;
	}
	
	// Entity 轉 DTO (For賣家)
	public OrderDto toDtoForSeller(Order order, Integer sellerId) {
		
		// 檢查該訂單是否屬於該賣家（直接比較 Order的sellerId）
		if(!sellerId.equals(order.getSellerId())) {
			return null;
		}
		// 如果是屬於該賣家，就轉換整筆訂單(toDto)
		return toDto(order);
	}
	
	// DTO 轉 Entity
	public Order toEntity(OrderDto orderDto) {
		Order order = modelMapper.map(orderDto, Order.class);
		
		order.setOrderTime(orderDto.getOrderTime());
		order.setCompletedTime(orderDto.getCompletedTime());
		
		return order;
	}
	
	// update的訂單狀態
	public void updateStatus(Order order, String newStatus) {
		order.setStatus(newStatus);
		
		// 如果設為已完成 -> 更新完成時間
		if ("已完成".equals(newStatus)) {
			order.setCompletedTime(java.time.LocalDateTime.now());
		}
	}

}
