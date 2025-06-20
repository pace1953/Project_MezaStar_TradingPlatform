package com.example.demo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDto {
    private Integer orderItemId;
    private Integer cardId;
    private Integer orderId;
    private Integer quantity;
    private int unitPrice;
    private int subtotal;
    
    // card 關聯欄位
    private String cardName;
    private String starLevel;
    private String series;
    private String sellerName;
    
    // order 關聯欄位
    private String orderNumber;
    private String orderStatus;
    private String buyerName;
    
}