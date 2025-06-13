package com.example.demo.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.demo.model.dto.CartDto;
import com.example.demo.model.entity.Cart;

@Component
public class CartMapper {
	
	@Autowired
	private ModelMapper modelMapper;

	
	// Entity -轉-> DTO
	public CartDto toDto(Cart cart) {
		CartDto cartDto = modelMapper.map(cart, CartDto.class);

		if(cart.getCard() != null) {
			cartDto.setCardName(cart.getCard().getCardName());
			cartDto.setCardPrice(cart.getCard().getPrice());
			cartDto.setStarLevel(cart.getCard().getStarLevel());
			cartDto.setSeries(cart.getCard().getSeries());
			cartDto.setSubtotal(cart.getCard().getPrice() * cart.getQuantity());
			
			if(cart.getCard().getSeller() != null) {
				cartDto.setSellerName(cart.getCard().getSeller().getUserName());
			}
			
		}
		
		return cartDto;
	}
	
	// DTO -轉-> Entity
	public Cart toEntity(CartDto cartDto) {
		return modelMapper.map(cartDto, Cart.class);
	}
	
	// 更新購物車中ㄉ卡匣數量
	public void updateQuantity(Cart cart, Integer newQuantity) {
		cart.setQuantity(newQuantity);
	}
}
