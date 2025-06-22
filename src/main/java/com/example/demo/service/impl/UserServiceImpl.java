package com.example.demo.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.mapper.UserMapper;
import com.example.demo.model.dto.UserDto;
import com.example.demo.model.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;
import com.example.demo.util.BCrypt;


@Service
public class UserServiceImpl implements UserService{

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UserMapper userMapper;
	
	@Override
	public UserDto getUser(String username) {
		User user = userRepository.getUser(username);
		if(user == null) {
			return null;
		}
		return userMapper.toDto(user);
	}

	@Override
	public void addUser(String username, String password, String email, Boolean active, String role) {
		
		String passwordHash = BCrypt.getBCrypt(password);
		
		User user = new User(null, username, passwordHash, email, active, role, null, null, null);
		
		userRepository.save(user);
		
	}

	@Override
	public void registerUser(String username, String password, String email) {
		
		User user = new User();
		user.setUserName(username);
		
		// 取 Hash
		String hashPassword = BCrypt.getBCrypt(password); // 取 hash 密碼
		user.setPasswordHash(hashPassword);
		user.setEmail(email);
		userRepository.save(user);
		
	}
	
    @Override
    public Optional<UserDto> updateUser(Integer userId, UserDto userDto) {
        return userRepository.findById(userId)
                .map(existingUser -> {
                    userMapper.updateEntity(userDto, existingUser);
                    User updatedUser = userRepository.save(existingUser);
                    return userMapper.toDto(updatedUser);
                });
    }

}
