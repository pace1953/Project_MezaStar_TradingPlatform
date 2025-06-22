package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.model.dto.OrderDto;
import com.example.demo.model.dto.UserCert;
import com.example.demo.response.ApiResponse;
import com.example.demo.service.OrderService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/rest/orders")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:5174"}, allowCredentials = "true")
public class OrderRestController {
    
    @Autowired
    private OrderService orderService;
    
    // 查詢我的訂單(買家)
    @GetMapping("/buyer")
    public ResponseEntity<ApiResponse<List<OrderDto>>> getMyOrders(HttpSession session) {
        UserCert userCert = (UserCert) session.getAttribute("userCert");
        if (userCert == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error(401, "請先登入"));
        }
        
        List<OrderDto> orders = orderService.findMyOrders(userCert.getUserId());
        return ResponseEntity.ok(ApiResponse.success("查詢成功", orders));
    }

    // 從購物車建立訂單（結帳）
    @PostMapping("/checkout")
    public ResponseEntity<ApiResponse<List<OrderDto>>> checkout(HttpSession session) {
        UserCert userCert = (UserCert) session.getAttribute("userCert");
        if (userCert == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error(401, "請先登入"));
        }
        
        try {
            List<OrderDto> orders = orderService.createOrderFromCart(userCert.getUserId());
            return ResponseEntity.ok(ApiResponse.success("結帳成功", orders));
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(400, "結帳失敗: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(500, "系統錯誤: " + e.getMessage()));
        }
    }
    
    // ------ 賣家 ------
    // 查詢我的訂單(賣家)
    @GetMapping("/seller")
    public ResponseEntity<ApiResponse<List<OrderDto>>> getMySales(HttpSession session) {
        UserCert userCert = (UserCert) session.getAttribute("userCert");
        if (userCert == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error(401, "請先登入"));
        }
        
        List<OrderDto> orders = orderService.findMySoldOrders(userCert.getUserId());
        return ResponseEntity.ok(ApiResponse.success("查詢成功", orders));
    }


    // 根據訂單狀態查詢賣家的訂單
    @GetMapping("/seller/status/{status}")
    public ResponseEntity<ApiResponse<List<OrderDto>>> getMySalesByStatus(
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
        
        List<OrderDto> orders = orderService.findSoldOrdersByStatus(userCert.getUserId(), status);
        return ResponseEntity.ok(ApiResponse.success("查詢成功", orders));
    }
    
    private boolean isValidOrderStatus(String status) {
        return status != null && 
               (status.equals("待處理") || status.equals("已完成") || status.equals("已取消"));
    }
}


