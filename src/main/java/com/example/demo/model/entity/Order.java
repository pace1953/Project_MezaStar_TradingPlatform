package com.example.demo.model.entity;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class Order {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Integer orderId;
    
    @Column(name = "order_number", unique = true, nullable = false, length = 50)
    private String orderNumber;
    
    @Column(name = "buyer_id", nullable = false)
    private Integer buyerId;
    
    @Column(name = "total_amount", nullable = false)
    private int totalAmount;
    
    @Column(name = "total_items", nullable = false)
    private Integer totalItems;
    
	// 訂單狀態：待處理、已完成、已取消
    @Column(name = "status", nullable = false, length = 20)
    private String status = "待處理"; 
    
    @Column(name = "order_time", nullable = false)
    private LocalDateTime orderTime;
    
    @Column(name = "completed_time")
    private LocalDateTime completedTime;
    
    @PrePersist
    protected void onCreate() {
        orderTime = LocalDateTime.now();
        if (status == null) {
            status = "待處理";
        }
        if (orderNumber == null) {
            orderNumber = "ORD" + System.currentTimeMillis();
        }
    }
    
    // 與buyer的關聯 (多對一)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyer_id", insertable = false, updatable = false)
    private User buyer;
    
    // 與OrderItem的關聯 (一對多)
    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY)
    private List<OrderItem> orderItems;
}
