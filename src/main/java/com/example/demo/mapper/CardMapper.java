package com.example.demo.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.example.demo.model.dto.CardDto;
import com.example.demo.model.entity.Card;
import com.example.demo.repository.UserRepository;

@Component
public class CardMapper {
	
	@Autowired
	private UserRepository userRepository;
	
	// Entity -> DTO
	public CardDto toDto(Card card) {
		if (card == null) {
			return null;
		}
		
		CardDto dto = new CardDto();
		dto.setCardId(card.getCardId());
		dto.setCardName(card.getCardName());
		dto.setSeries(card.getSeries());
		dto.setStarLevel(card.getStarLevel());
		dto.setPrice(card.getPrice());
		dto.setSellerId(card.getSellerId());
		dto.setStatus(card.getStatus());
		dto.setCreatedAt(card.getCreatedAt());
		dto.setUpdatedAt(card.getUpdatedAt());
		dto.setQuantity(card.getQuantity());
		dto.setAvailableQuantity(card.getAvailableQuantity());
		
		try {
			if (card.getSellerId() != null) {
				var seller = userRepository.findByUserId(card.getSellerId());
				if (seller.isPresent()) {
					dto.setSellerName(seller.get().getUserName());
				} else {
					dto.setSellerName("找不到賣家");
				}
			} else {
				dto.setSellerName("未知賣家");
			}
		} catch (Exception e) {
			// 如果查詢失敗，設定預設值
			dto.setSellerName("查詢失敗");
		}
		
		return dto;
	}
	
	// DTO -> Entity
	public Card toEntity(CardDto dto) {
		if (dto == null) {
			return null;
		}
		
		Card card = new Card();
		
		if (dto.getCardId() != null) {
			card.setCardId(dto.getCardId());
		}
		card.setCardName(dto.getCardName());
		card.setSeries(dto.getSeries());
		card.setStarLevel(dto.getStarLevel());
		card.setPrice(dto.getPrice());
		card.setSellerId(dto.getSellerId());
		card.setStatus(dto.getStatus() != null ? dto.getStatus() : "上架中");
		card.setCreatedAt(dto.getCreatedAt());
		card.setUpdatedAt(dto.getUpdatedAt());
		card.setQuantity(dto.getQuantity());
		card.setAvailableQuantity(dto.getAvailableQuantity());
		
		return card;
	}
	
	// 更新現有 Entity
	public void updateEntity(CardDto dto, Card card) {
		if (dto == null || card == null) {
			return;
		}
		
		if (dto.getCardName() != null) {
			card.setCardName(dto.getCardName());
		}
		if (dto.getSeries() != null) {
			card.setSeries(dto.getSeries());
		}
		if (dto.getStarLevel() != null) {
			card.setStarLevel(dto.getStarLevel());
		}
		if (dto.getPrice() != 0) {
			card.setPrice(dto.getPrice());
		}
		if (dto.getStatus() != null) {
			card.setStatus(dto.getStatus());
		}
		if (dto.getCreatedAt() != null) {
			card.setCreatedAt(dto.getCreatedAt());
		}
		if (dto.getUpdatedAt() != null) {
			card.setUpdatedAt(dto.getUpdatedAt());
		}
		if (dto.getQuantity() != null) {
			card.setQuantity(dto.getQuantity());
		}
		if (dto.getAvailableQuantity() != null) {
			card.setAvailableQuantity(dto.getAvailableQuantity());
		}
	}
}