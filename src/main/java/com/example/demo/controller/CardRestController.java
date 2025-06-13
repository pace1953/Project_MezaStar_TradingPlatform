package com.example.demo.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.model.dto.CardDto;
import com.example.demo.model.dto.UserCert;
import com.example.demo.response.ApiResponse;
import com.example.demo.service.CardService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/rest/cards")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:5174"}, allowCredentials = "true")
public class CardRestController {
	
    @Autowired
    private CardService cardService;
    
    @GetMapping
    public ResponseEntity<ApiResponse<List<CardDto>>> getAvailableCards() {
        List<CardDto> cards = cardService.findAvailableCards();
        return ResponseEntity.ok(ApiResponse.success("查詢成功", cards));
    }
    
    // 根據卡匣ID進行查詢
    @GetMapping("/{cardId}")
    public ResponseEntity<ApiResponse<CardDto>> getCardById(@PathVariable Integer cardId) {
        Optional<CardDto> card = cardService.findCardById(cardId);
        if (card.isPresent()) {
            return ResponseEntity.ok(ApiResponse.success("查詢成功", card.get()));
        } else {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(404, "找不到該卡匣"));
        }
    }

    // 多條件搜尋
    @GetMapping("/search-multiCondition")
    public ResponseEntity<ApiResponse<List<CardDto>>> searchCardsByMultiCondition(
    		@RequestParam(required = false) String series, 
    		@RequestParam(required = false) String starLevel, 
    		@RequestParam(required = false) String keyword,
    		@RequestParam(required = false) Integer minPrice,
    		@RequestParam(required = false) Integer maxPrice){
    	List<CardDto> cards = cardService.searchCardsByMultipleConditions(series, starLevel, keyword, minPrice, maxPrice);
    	return ResponseEntity.ok(ApiResponse.success("查詢成功", cards));
    }
    
    
    // 查詢我的卡匣（需要登入）
    @GetMapping("/my-cards")
    public ResponseEntity<ApiResponse<List<CardDto>>> getMyCards(HttpSession session) {
        UserCert userCert = (UserCert) session.getAttribute("userCert");
        if (userCert == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error(401, "請先登入"));
        }
        
        List<CardDto> cards = cardService.findMyCards(userCert.getUserId());
        return ResponseEntity.ok(ApiResponse.success("查詢成功", cards));
    }
    
    // 新增卡匣（需要登入）
    @PostMapping
    public ResponseEntity<ApiResponse<CardDto>> createCard(@RequestBody CardDto cardDto, HttpSession session) {
        UserCert userCert = (UserCert) session.getAttribute("userCert");
        if (userCert == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error(401, "請先登入"));
        }
        
        try {
            cardDto.setSellerId(userCert.getUserId());
            CardDto createdCard = cardService.createCard(cardDto);
            return ResponseEntity.ok(ApiResponse.success("新增成功", createdCard));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(400, "新增失敗: " + e.getMessage()));
        }
    }
    
    // 更新卡匣（需要登入且為卡匣擁有者）
    @PutMapping("/{cardId}")
    public ResponseEntity<ApiResponse<CardDto>> updateCard(
            @PathVariable Integer cardId, 
            @RequestBody CardDto cardDto, 
            HttpSession session) {
        UserCert userCert = (UserCert) session.getAttribute("userCert");
        if (userCert == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error(401, "請先登入"));
        }
        
        // 檢查是否為卡匣擁有者
        Optional<CardDto> existingCard = cardService.findCardById(cardId);
        if (existingCard.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(404, "找不到該卡匣"));
        }
        
        if (!existingCard.get().getSellerId().equals(userCert.getUserId())) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.error(403, "只能修改自己的卡匣"));
        }
        
        try {
            Optional<CardDto> updatedCard = cardService.updateCard(cardId, cardDto);
            if (updatedCard.isPresent()) {
                return ResponseEntity.ok(ApiResponse.success("更新成功", updatedCard.get()));
            } else {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error(404, "更新失敗"));
            }
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(400, "更新失敗: " + e.getMessage()));
        }
    }
    
    // 刪除卡匣（需要登入且為卡匣擁有者）
    @DeleteMapping("/{cardId}")
    public ResponseEntity<ApiResponse<Void>> deleteCard(@PathVariable Integer cardId, HttpSession session) {
        UserCert userCert = (UserCert) session.getAttribute("userCert");
        if (userCert == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error(401, "請先登入"));
        }
        
        // 檢查是否為卡匣擁有者
        Optional<CardDto> existingCard = cardService.findCardById(cardId);
        if (existingCard.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(404, "找不到該卡匣"));
        }
        
        if (!existingCard.get().getSellerId().equals(userCert.getUserId())) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.error(403, "只能刪除自己的卡匣"));
        }
        
        boolean deleted = cardService.deleteCard(cardId);
        if (deleted) {
            return ResponseEntity.ok(ApiResponse.success("刪除成功", null));
        } else {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(404, "刪除失敗"));
        }
    }
    
    
}
