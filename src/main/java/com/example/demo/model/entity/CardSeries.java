package com.example.demo.model.entity;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "card_series")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardSeries {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer seriesId;  // 改成 seriesId
    
    @Column(name = "series_name")
    private String seriesName;
    
    @OneToMany(mappedBy = "series")
    private List<Card> cards;
}