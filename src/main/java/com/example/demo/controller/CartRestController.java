package com.example.demo.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.model.dto.CartDto;
import com.example.demo.model.dto.UserCert;
import com.example.demo.response.ApiResponse;
import com.example.demo.service.CartService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/rest/cart")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:5174"}, allowCredentials = "true")
public class CartRestController {
    
    @Autowired
    private CartService cartService;
    
    // 查詢我的購物車
    @GetMapping
    public ResponseEntity<ApiResponse<List<CartDto>>> getMyCart(HttpSession session) {
        UserCert userCert = (UserCert) session.getAttribute("userCert");
        if (userCert == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error(401, "請先登入"));
        }
        
        List<CartDto> cartItems = cartService.findMyCart(userCert.getUserId());
        return ResponseEntity.ok(ApiResponse.success("查詢成功", cartItems));
    }
    
    // 新增到購物車
    @PostMapping
    public ResponseEntity<ApiResponse<CartDto>> addToCart(@RequestBody CartDto cartDto, HttpSession session) {
        UserCert userCert = (UserCert) session.getAttribute("userCert");
        if (userCert == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error(401, "請先登入"));
        }
        
        try {
            // 設定買家ID
            cartDto.setBuyerId(userCert.getUserId());
            CartDto addedItem = cartService.addToCart(cartDto);
            return ResponseEntity.ok(ApiResponse.success("加入購物車成功", addedItem));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(400, "加入購物車失敗: " + e.getMessage()));
        }
    }
    
    // 更新購物車數量
    @PutMapping("/{cartId}")
    public ResponseEntity<ApiResponse<CartDto>> updateCartQuantity(
            @PathVariable Integer cartId, 
            @RequestBody CartDto cartDto, 
            HttpSession session) {
        UserCert userCert = (UserCert) session.getAttribute("userCert");
        if (userCert == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error(401, "請先登入"));
        }
        
        try {
            Optional<CartDto> updatedItem = cartService.updateCartQuantity(cartId, cartDto.getQuantity());
            if (updatedItem.isPresent()) {
                return ResponseEntity.ok(ApiResponse.success("更新成功", updatedItem.get()));
            } else {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error(404, "找不到該購物車項目"));
            }
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(400, "更新失敗: " + e.getMessage()));
        }
    }
    
    // 從購物車移除
    @DeleteMapping("/{cartId}")
    public ResponseEntity<ApiResponse<Void>> removeFromCart(@PathVariable Integer cartId, HttpSession session) {
        UserCert userCert = (UserCert) session.getAttribute("userCert");
        if (userCert == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error(401, "請先登入"));
        }
        
        boolean removed = cartService.removeFromCart(cartId);
        if (removed) {
            return ResponseEntity.ok(ApiResponse.success("移除成功", null));
        } else {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(404, "移除失敗"));
        }
    }
    
    // 清空購物車
    @DeleteMapping("/clear")
    public ResponseEntity<ApiResponse<Void>> clearCart(HttpSession session) {
        UserCert userCert = (UserCert) session.getAttribute("userCert");
        if (userCert == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error(401, "請先登入"));
        }
        
        cartService.clearCart(userCert.getUserId());
        return ResponseEntity.ok(ApiResponse.success("清空購物車成功", null));
    }
}