package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import com.example.demo.exception.CardException;
import com.example.demo.exception.CartException;
import com.example.demo.model.dto.CartDto;

public interface CartService {
	
    List<CartDto> findMyCart(Integer buyerId);
    CartDto addToCart(CartDto cartDto) throws CardException, CartException;
    Optional<CartDto> updateCartQuantity(Integer cartId, Integer quantity);
    boolean removeFromCart(Integer cartId);
    void clearCart(Integer buyerId);
    boolean isInCart(Integer buyerId, Integer cardId);
	boolean checkAvailableQuantity(Integer cardId, Integer requestedQuantity);
}
