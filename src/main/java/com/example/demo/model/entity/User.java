package com.example.demo.model.entity;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {
	
	@Id // PrimaryKey
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private Integer userId;
	
	@Column(name = "username", unique = true, nullable = false, length = 50) 
	// unique = true(唯一性，不可重複)
	private String userName;
	
	@Column(name = "password_hash", nullable = false)
	private String passwordHash;
	
	@Column(name = "salt", nullable = false)
	private String salt;
	
	@Column(name = "email", unique = true, nullable = false)
	// unique = true(唯一性，不可重複)
	private String email;
	
	@Column(name = "active")
	private Boolean active;
	
	@Column(name = "role")
	private String role;
	
	// seller的Card (一對多)
	@OneToMany(mappedBy = "seller", fetch = FetchType.LAZY)
	private List<Card> sellingCards;
	
	// buyer的Cart (一對多)
	@OneToMany(mappedBy = "buyer", fetch = FetchType.LAZY)
	private List<Cart> carts;
	
	// buyer的Order (一對多)
	@OneToMany(mappedBy = "buyer", fetch = FetchType.LAZY)
	private List<Order> orders;
	
}
