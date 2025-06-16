package com.example.demo.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.mapper.CardMapper;
import com.example.demo.model.dto.CardDto;
import com.example.demo.model.entity.Card;
import com.example.demo.repository.CardRepository;
import com.example.demo.service.CardService;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class CardServiceImpl implements CardService{
	
    @Autowired
    private CardRepository cardRepository;
    
    @Autowired
    private CardMapper cardMapper;
    
    @Override
    @Transactional
    public List<CardDto> findAllCards() {
        return cardRepository.findAll().stream()
                .map(cardMapper::toDto)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional
    public Optional<CardDto> findCardById(Integer cardId) {
        return cardRepository.findById(cardId)
                .map(cardMapper::toDto);
    }
    
    @Override
    @Transactional
    public List<CardDto> findAvailableCards() {
        return cardRepository.findByStatus("上架中").stream()
                .map(cardMapper::toDto)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional
    public List<CardDto> findMyCards(Integer sellerId) {
        return cardRepository.findBySellerId(sellerId).stream()
                .map(cardMapper::toDto)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional
    public CardDto createCard(CardDto cardDto) {
        Card card = cardMapper.toEntity(cardDto);
        Card savedCard = cardRepository.save(card);
        return cardMapper.toDto(savedCard);
    }
    
    @Override
    @Transactional
    public Optional<CardDto> updateCard(Integer cardId, CardDto cardDto) {
        return cardRepository.findById(cardId)
                .map(existingCard -> {
                    cardMapper.updateEntity(cardDto, existingCard);
                    Card updatedCard = cardRepository.save(existingCard);
                    return cardMapper.toDto(updatedCard);
                });
    }
    
    @Override
    @Transactional
    public boolean deleteCard(Integer cardId) {
        if (cardRepository.existsById(cardId)) {
            cardRepository.deleteById(cardId);
            return true;
        }
        return false;
    }
    
    @Override
    @Transactional
    public Optional<CardDto> markCardAsSold(Integer cardId) {
        return cardRepository.findById(cardId)
                .map(card -> {
                    card.setStatus("已售出");
                    Card updatedCard = cardRepository.save(card);
                    return cardMapper.toDto(updatedCard);
                });
    }

	@Override
	@Transactional
	public List<CardDto> searchCardsByMultipleConditions(String series, String starLevel, String keyword,
			Integer minPrice, Integer maxPrice) {
		return cardRepository.findCardByMultipleConditions(series, starLevel, keyword, minPrice, maxPrice)
				.stream().map(cardMapper::toDto).collect(Collectors.toList());
	}
}
