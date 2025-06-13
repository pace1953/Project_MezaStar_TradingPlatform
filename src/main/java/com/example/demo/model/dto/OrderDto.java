package com.example.demo.model.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {
    private Integer orderId;
    private String orderNumber;
    private Integer buyerId;
    private String buyerName;
    private int totalAmount;
    private Integer totalItems;
    private String status;
    private LocalDateTime orderTime;
    private LocalDateTime completedTime;
    private List<OrderItemDto> orderItems;
}
