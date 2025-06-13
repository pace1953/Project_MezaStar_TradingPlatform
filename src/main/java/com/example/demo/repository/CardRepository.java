package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.model.entity.Card;

@Repository
public interface CardRepository extends JpaRepository<Card, Integer>{
	
    // 根據交易狀態查詢卡匣
	List<Card> findByStatus(String status);
    
    // 賣家查看自己上架的卡匣
	List<Card> findBySellerId(Integer sellerId);
    
    // 多條件搜尋
    @Query("SELECT c FROM Card c WHERE " +
    	       "(:series IS NULL OR c.series = :series) AND " +
    	       "(:starLevel IS NULL OR c.starLevel = :starLevel) AND " +
    	       "(:keyword IS NULL OR c.cardName LIKE CONCAT('%', :keyword, '%')) AND " +
    	       "(:minPrice IS NULL OR c.price >= :minPrice) AND " +
    	       "(:maxPrice IS NULL OR c.price <= :maxPrice) AND " +
    	       "c.status = '上架中'")
    List<Card> findCardByMultipleConditions(@Param("series") String series, 
    										@Param("starLevel") String starLevel, 
    										@Param("keyword") String keyword, 
    										@Param("minPrice") Integer minPrice, 
    										@Param("maxPrice") Integer maxPrice);
}
