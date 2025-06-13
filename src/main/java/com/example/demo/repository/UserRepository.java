package com.example.demo.repository;

import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.demo.model.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>{
	// 預設會實現 CRUD
	// 自定義查詢(3種寫法)
	// 1. 自動生成SQL語法
	List<User> findByUserName(String userName);
	
	// 2. T-SQL
	@Query(value = "select user_id, username, password_hash, salt, email, active, role from users where username=:username", nativeQuery = true)
	User getUser(String username);
	
	// 3. (Java)PQL
	@Query(value = "select u from User u")  // 使用 u 作為別名
	List<User> getAllUsers();
	
    // 根據使用者ID查詢
    Optional<User> findByUserId(Integer userId);
    
    // 根據email查詢
    Optional<User> findByEmail(String email);
	
}
