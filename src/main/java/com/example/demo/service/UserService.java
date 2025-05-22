package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.model.dto.UserDto;


public interface UserService {
	public UserDto getUser(String username);
	public void addUser(String username, String password, String email, Boolean active, String role);
	// 可以往下自訂...(修改/刪除)
	public List<UserDto> findAllUser();
}
