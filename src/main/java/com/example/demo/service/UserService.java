package com.example.demo.service;

import java.util.Optional;

import com.example.demo.model.dto.UserDto;


public interface UserService {
	
	public UserDto getUser(String username);
	
	public void addUser(String username, String password, String email, Boolean active, String role);
	
	// 註冊
	public void registerUser(String username, String password, String email);
	
	Optional<UserDto> updateUser(Integer userId, UserDto userDto);
}
