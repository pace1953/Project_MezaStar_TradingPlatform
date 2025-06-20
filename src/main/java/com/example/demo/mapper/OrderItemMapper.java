package com.example.demo.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.demo.model.dto.OrderItemDto;
import com.example.demo.model.entity.Card;
import com.example.demo.model.entity.Cart;
import com.example.demo.model.entity.OrderItem;

@Component
public class OrderItemMapper {

	@Autowired
	private ModelMapper modelMapper;
	
	// Entity 轉 DTO
	public OrderItemDto toDto(OrderItem orderItem) {
		OrderItemDto orderItemDto = modelMapper.map(orderItem, OrderItemDto.class);
		
		// 如果 card 關聯已載入 -> 手動設定卡匣相關資訊
		if (orderItem.getCard() != null) {
			Card card = orderItem.getCard();
			orderItemDto.setCardName(card.getCardName());
			orderItemDto.setStarLevel(card.getStarLevel());
			orderItemDto.setSeries(card.getSeries());
			
			// 如果 seller 關聯已載入 -> 設定賣家名稱
			if (card.getSeller() != null) {
				orderItemDto.setSellerName(card.getSeller().getUserName());
			}
		}
		
		// 如果 order 關聯已載入 -> 設定訂單資訊
		if(orderItem.getOrder() != null) {
			orderItemDto.setOrderNumber(orderItem.getOrder().getOrderNumber());
			orderItemDto.setOrderStatus(orderItem.getOrder().getStatus());
			// 如果 buyer 關聯已載入 -> 設定買家名稱
			if(orderItem.getOrder().getBuyer() != null) {
				orderItemDto.setBuyerName(orderItem.getOrder().getBuyer().getUserName());
			}
		}
		
		return orderItemDto;
	}
	
	// DTO 轉 Entity
	public OrderItem toEntity(OrderItemDto orderItemDto) {
		return modelMapper.map(orderItemDto, OrderItem.class);
	}
	
	// 建立OrderItem的明細 (透過購物車的明細)
	public OrderItem createFromCart(Cart cartItem, Integer orderId) {
		OrderItem orderItem = new OrderItem();
		orderItem.setOrderId(orderId);
		orderItem.setCardId(cartItem.getCardId());
		orderItem.setQuantity(cartItem.getQuantity());
		
		// 從卡匣取得當前價格作為單價
		if (cartItem.getCard() != null) {
			orderItem.setUnitPrice(cartItem.getCard().getPrice());
			orderItem.setSubtotal(cartItem.getCard().getPrice() * cartItem.getQuantity());
		}
		return orderItem;
	}
}

