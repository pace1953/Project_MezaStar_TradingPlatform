package com.example.demo.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
	private Integer cardId;
	
    @Column(name = "card_number")
    private String cardNumber;
    
    @Column(name = "card_name")
    private String cardName;
    
    @Column(name = "star_level")
    private Integer starLevel;
    
    @ManyToOne
    @JoinColumn(name = "series_id")  // 外鍵欄位名稱
    private CardSeries series;
	
}
