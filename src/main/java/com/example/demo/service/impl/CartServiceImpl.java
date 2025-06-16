package com.example.demo.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.exception.CardException;
import com.example.demo.exception.CartException;
import com.example.demo.mapper.CartMapper;
import com.example.demo.model.dto.CartDto;
import com.example.demo.model.entity.Card;
import com.example.demo.model.entity.Cart;
import com.example.demo.repository.CardRepository;
import com.example.demo.repository.CartRepository;
import com.example.demo.service.CartService;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class CartServiceImpl implements CartService {
    
    @Autowired
    private CartRepository cartRepository;
    
    @Autowired
    private CartMapper cartMapper;
    
    @Autowired
    private CardRepository cardRepository;
    
    @Override
    @Transactional
    public List<CartDto> findMyCart(Integer buyerId) {
        return cartRepository.findByBuyerIdWithDetails(buyerId).stream()
                .map(cartMapper::toDto)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional
    public CartDto addToCart(CartDto cartDto) throws CardException, CartException {
    	
    	// 檢查卡匣是否存在，若存在 -> 檢查是否還有庫存
    	Optional<Card> optCard = cardRepository.findById(cartDto.getCardId());
    	if(optCard.isEmpty()) {
    		throw new CardException("卡匣不存在");
    	}
    	
    	Card card = optCard.get();
    	// 防止自己購買自己的商品
    	if(card.getSellerId().equals(cartDto.getBuyerId())) {
    		throw new CartException("不能將自己販賣的卡匣加入購物車");
    	}
    	
    	// 卡匣是否還有庫存
    	if(!"上架中".equals(card.getStatus())) {
    		throw new CardException("卡匣已下架或售出");
    	}
    	
        // 檢查購物車中是否已存在卡匣
        Optional<Cart> existingCart = cartRepository.findByBuyerIdAndCardId(
                cartDto.getBuyerId(), cartDto.getCardId());
        
        // 初始化 -> 檢查現有的數量
        int requestedQuantity = cartDto.getQuantity();
        int currentCardQuantity = 0;
        if(existingCart.isPresent()) {
        	currentCardQuantity = existingCart.get().getQuantity();
        }
        
        // 檢查總需求數量是否超過可用的庫存數量
        int totalRequestedQuantity = currentCardQuantity + requestedQuantity;
        if(totalRequestedQuantity > card.getAvailableQuantity()) {
        	throw new CartException("庫存不足，目前可用數量為 "+ card.getAvailableQuantity()
        	+"，購物車內已有 "+currentCardQuantity);
        }
        
        if (existingCart.isPresent()) {
            // 如果卡匣已存在，則更新購物車中的數量
            Cart cart = existingCart.get();
            cart.setQuantity(cart.getQuantity() + cartDto.getQuantity());
            Cart updatedCart = cartRepository.save(cart);
            return cartMapper.toDto(updatedCart);
        } else {
            // 如果卡匣不存在，則新增到購物車
            Cart cart = cartMapper.toEntity(cartDto);
            Cart savedCart = cartRepository.save(cart);
            return cartMapper.toDto(savedCart);
        }
    }
    
    @Override
    @Transactional
    public Optional<CartDto> updateCartQuantity(Integer cartId, Integer quantity) {
        return cartRepository.findById(cartId)
                .map(cart -> {
                    cartMapper.updateQuantity(cart, quantity);
                    Cart updatedCart = cartRepository.save(cart);
                    return cartMapper.toDto(updatedCart);
                });
    }
    
    @Override
    @Transactional
    public boolean checkAvailableQuantity(Integer cardId, Integer requestedQuantity) {
    	Optional<Card> optCard = cardRepository.findById(cardId);
    	
    	if(optCard.isEmpty()) {
    		return false;
    	}
    	Card card = optCard.get();
    	return card.getAvailableQuantity() >= requestedQuantity && "上架中".equals(card.getStatus());
    }
    
    @Override
    @Transactional
    public boolean removeFromCart(Integer cartId) {
        if (cartRepository.existsById(cartId)) {
            cartRepository.deleteById(cartId);
            return true;
        }
        return false;
    }
    
    @Override
    @Transactional
    public void clearCart(Integer buyerId) {
        cartRepository.deleteByBuyerId(buyerId);
    }
    
    @Override
    @Transactional
    public boolean isInCart(Integer buyerId, Integer cardId) {
        return cartRepository.existsByBuyerIdAndCardId(buyerId, cardId);
    }
}
