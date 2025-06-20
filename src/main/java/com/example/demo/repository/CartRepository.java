package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.model.entity.Cart;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer>{
	
    // 買家查詢購物車 - 使用 JOIN FETCH 一次載入所有需要的資料
    @Query("SELECT c FROM Cart c " +
            "JOIN FETCH c.card card " +
            "JOIN FETCH card.seller " +
            "WHERE c.buyerId = :buyerId")
     List<Cart> findByBuyerId(@Param("buyerId") Integer buyerId);
    
    // 根據買家和卡匣查詢（檢查是否已在購物車）
    Optional<Cart> findByBuyerIdAndCardId(Integer buyerId, Integer cardId);
    
    // 檢查卡匣是否在某買家的購物車中
    boolean existsByBuyerIdAndCardId(Integer buyerId, Integer cardId);
    
    
    // 根據買家ID刪除購物車（結帳後清空）
    @Modifying
    @Transactional
    @Query("DELETE FROM Cart c WHERE c.buyerId = :buyerId")
    void deleteByBuyerId(Integer buyerId);

}
