package com.example.demo.model.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CardDto {
	
    private Integer cardId;
    private String cardName;
    private String series;
    private String starLevel;
    private int price;
    private String status;
    private Integer sellerId;
    private String sellerName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer quantity;
    private Integer availableQuantity;
    
}
