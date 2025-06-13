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
    
    // 查詢訂單明細
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
}