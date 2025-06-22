package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import com.example.demo.model.dto.CardDto;

public interface CardService {

    Optional<CardDto> findCardById(Integer cardId);
    List<CardDto> findAvailableCards();
    // 多條件搜尋卡匣
    List<CardDto> searchCardsByMultipleConditions(String series, String starLevel, String keyword
    		, Integer minPrice, Integer maxPrice);
    // 查詢我的卡匣
    List<CardDto> findMyCards(Integer sellerId);
    CardDto createCard(CardDto cardDto);
    Optional<CardDto> updateCard(Integer cardId, CardDto cardDto);
    boolean deleteCard(Integer cardId);
    Optional<CardDto> markCardAsSold(Integer cardId);
}
