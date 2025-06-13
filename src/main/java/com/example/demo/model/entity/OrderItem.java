package com.example.demo.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "order_items")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_item_id")
    private Integer orderItemId;
    
    @Column(name = "order_id", nullable = false)
    private Integer orderId;
    
    @Column(name = "card_id", nullable = false)
    private Integer cardId;
    
    @Column(name = "quantity", nullable = false)
    private Integer quantity;
    
    @Column(name = "unit_price", nullable = false, scale = 2)
    private int unitPrice;
    
    @Column(name = "subtotal", nullable = false, scale = 2)
    private int subtotal;
    
    @PrePersist
    @PreUpdate
    protected void calculateSubtotal() {
        if (unitPrice != 0 && quantity != null) {
            subtotal = unitPrice * quantity;
        }
    }
    
    // 與Order的關聯 (多對一)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", insertable = false, updatable = false)
    private Order order;
    
    // 與Card的關聯 (多對一)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_id", insertable = false, updatable = false)
    private Card card;
}
