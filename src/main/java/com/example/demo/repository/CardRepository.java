package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.model.entity.Card;

@Repository
public interface CardRepository {

	@Query(value = "select sL from  Card sL")
	List<Card> fingCardsByStarLevel();
}
