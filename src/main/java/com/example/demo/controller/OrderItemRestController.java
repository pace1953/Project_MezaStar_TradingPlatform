package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.model.dto.OrderItemDto;
import com.example.demo.model.dto.UserCert;
import com.example.demo.response.ApiResponse;
import com.example.demo.service.OrderItemService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/rest/order-items")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:5174"}, allowCredentials = "true")
public class OrderItemRestController {
    
    @Autowired
    private OrderItemService orderItemService;
    
    // 查詢訂單明細(買家)
    @GetMapping("/order/{orderId}")
    public ResponseEntity<ApiResponse<List<OrderItemDto>>> getOrderItems(@PathVariable Integer orderId, HttpSession session) {
        UserCert userCert = (UserCert) session.getAttribute("userCert");
        if (userCert == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error(401, "請先登入"));
        }
        
        List<OrderItemDto> orderItems = orderItemService.findOrderItems(orderId);
        return ResponseEntity.ok(ApiResponse.success("查詢成功", orderItems));
    }
    
    // ------ 賣家 ------
    // 查詢訂單明細(賣家)
    @GetMapping("/seller/my-sales")
    public ResponseEntity<ApiResponse<List<OrderItemDto>>> getMySellerItems(HttpSession session) {
        UserCert userCert = (UserCert) session.getAttribute("userCert");
        if (userCert == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error(401, "請先登入"));
        }
        
        System.out.println("賣家ID: " + userCert.getUserId()); // 加這行
        List<OrderItemDto> items = orderItemService.findSoldOrderItems(userCert.getUserId());
        System.out.println("查詢結果數量: " + items.size()); // 加這行
        return ResponseEntity.ok(ApiResponse.success("查詢成功", items));
    }
    
    // 根據訂單狀態查詢賣家的訂單明細
    @GetMapping("/seller/my-sales/status/{status}")
    public ResponseEntity<ApiResponse<List<OrderItemDto>>> getMySellerItemsByStatus(
            @PathVariable String status, HttpSession session) {
        UserCert userCert = (UserCert) session.getAttribute("userCert");
        if (userCert == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error(401, "請先登入"));
        }
        
        // 驗證狀態值
        if (!isValidOrderStatus(status)) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(400, "無效的訂單狀態"));
        }
        
        List<OrderItemDto> items = orderItemService.findSoldOrderByStatus(userCert.getUserId(), status);
        return ResponseEntity.ok(ApiResponse.success("查詢成功", items));
    }

    private boolean isValidOrderStatus(String status) {
        return status != null && 
               (status.equals("待處理") || status.equals("已完成") || status.equals("已取消"));
    }
    
    
}