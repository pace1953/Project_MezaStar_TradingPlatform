package com.example.demo.model.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartDto {

    private Integer cartId;
    private Integer buyerId;
    private Integer cardId;
    private String cardName;
    private int cardPrice;
    private String starLevel;
    private String series;
    private String sellerName;
    private Integer quantity;
    private int subtotal;
    private LocalDateTime addedAt;
}
