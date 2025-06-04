package com.example.demo.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.demo.model.dto.CardDto;
import com.example.demo.model.dto.CardSeriesDto;
import com.example.demo.model.entity.Card;
import com.example.demo.model.entity.CardSeries;

@Component
public class CardMapper {
	
	@Autowired
	private ModelMapper modelMapper;
	
	public CardDto toDto(Card card) {
		return modelMapper.map(card, CardDto.class);
		
	}
	
	public Card toEntity(CardDto cardDto) {
		return modelMapper.map(cardDto, Card.class);		
	}
	
	public CardSeriesDto seriestoDto(CardSeries cardSeries) {
		return modelMapper.map(cardSeries, CardSeriesDto.class);
	}
	
	public CardSeries seriestoEntity(CardSeriesDto cardSeriesDto) {
		return modelMapper.map(cardSeriesDto, CardSeries.class);
	}
}
