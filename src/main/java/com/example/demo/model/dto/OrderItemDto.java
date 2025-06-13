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
    private String cardName;
    private String starLevel;
    private String series;
    private Integer quantity;
    private int unitPrice;
    private int subtotal;
    private String sellerName;
}