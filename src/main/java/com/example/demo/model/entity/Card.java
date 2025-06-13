package com.example.demo.model.entity;

import java.time.LocalDateTime;
import java.util.List;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "cards")
public class Card {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "card_id")
    private Integer cardId;
    
    @Column(name = "card_name", nullable = false, length = 100)
    private String cardName;
    
    // 系列/彈種：星塵第1、2彈和MEZASTAR活動卡匣
    @Column(name = "series", nullable = false, length = 50)
    private String series; 
    
    // 星級：2-6星
    @Column(name = "star_level", nullable = false)
    private String starLevel; 
    
    @Column(name = "price", nullable = false)
    private int price;
    
    // 卡匣的數量
    @Column(name = "quantity", nullable = false)
    private Integer quantity;
    
    // 卡匣可獲得的數量
    @Column(name = "available_quantity", nullable = false)
    private Integer availableQuantity;
    
    // 卡匣販售的狀態：上架中、已售出
    @Column(name = "status", nullable = false, length = 20)
    private String status = "上架中"; 
    
    @Column(name = "seller_id", nullable = false)
    private Integer sellerId;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // 更新可獲得的卡匣數量
    public void updateAvailableQuantity(Integer soldQuantity) {
    	this.availableQuantity = this.availableQuantity - soldQuantity;
    	if(this.availableQuantity <= 0) {
    		this.status = "已售出";
    		this.availableQuantity = 0 ;
    	}
    }
    
    // 創建卡匣
    @PrePersist // -> 儲存到資料庫之前自動執行
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (status == null) {
            status = "上架中";
        }
        if(quantity == null) {
        	quantity = 1 ;
        }
        if(availableQuantity == null) {
        	availableQuantity = quantity;
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    // 與seller的關聯 (多對一)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id", insertable = false, updatable = false)
    private User seller;
    
    // 與Cart的關聯 (一對多)
    @OneToMany(mappedBy = "card", fetch = FetchType.LAZY)
    private List<Cart> carts;
    
    // 與OrderItem的關聯 (一對多)
    @OneToMany(mappedBy = "card", fetch = FetchType.LAZY)
    private List<OrderItem> orderItems;
}
