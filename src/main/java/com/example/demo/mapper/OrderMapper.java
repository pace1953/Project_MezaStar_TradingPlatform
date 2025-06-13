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
		
		// 如果 buyer 已載入 -> 手動設定買家名稱
		if (order.getBuyer() != null) {
			orderDto.setBuyerName(order.getBuyer().getUserName());
		}
		
		// 如果 orderItems 已載入 -> 轉換訂單明細
		if (order.getOrderItems() != null && !order.getOrderItems().isEmpty()) {
			List<OrderItemDto> orderItemDtos = order.getOrderItems().stream()
					.map(orderItemMapper::toDto)
					.collect(Collectors.toList());
			orderDto.setOrderItems(orderItemDtos);
		}
		return orderDto;
	}
	
	// DTO 轉 Entity
	public Order toEntity(OrderDto orderDto) {
		return modelMapper.map(orderDto, Order.class);
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
