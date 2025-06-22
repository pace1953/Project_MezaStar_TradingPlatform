package com.example.demo.model.entity;
import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "cart")
public class Cart {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_id")
    private Integer cartId;
    
    @Column(name = "buyer_id", nullable = false)
    private Integer buyerId;
    
    @Column(name = "card_id", nullable = false)
    private Integer cardId;
    
    // 購買的卡匣數量
    @Column(name = "quantity", nullable = false)
    private Integer quantity = 1;
    
    @Column(name = "added_at", nullable = false)
    private LocalDateTime addedAt;
    
    @PrePersist
    protected void onCreate() {
        addedAt = LocalDateTime.now();
        if (quantity == null) {
            quantity = 1;
        }
    }
    
    // 與buyer的關聯 (多對一)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyer_id", insertable = false, updatable = false)
    private User buyer;
    
    // 與Card的關聯 (多對一)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_id", insertable = false, updatable = false)
    private Card card;
}
