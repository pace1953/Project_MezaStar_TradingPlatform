package com.example.demo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CardDto {
	
	private Integer cardId;
	private String cardNumber;
	private String cardName;
	private Integer starLevel;
	private String seriesName;
}
