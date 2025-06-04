package com.example.demo.model.dto;

import java.util.List;

import com.example.demo.model.entity.Card;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CardSeriesDto {
	
	private Integer seriesId;
	private String seriesName;
	private List<Card> cards;
}
