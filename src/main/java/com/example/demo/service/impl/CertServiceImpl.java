package com.example.demo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.example.demo.exception.PasswordInvalidException;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.model.dto.UserCert;
import com.example.demo.model.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.CertService;
import com.example.demo.util.BCrypt;

@Service
public class CertServiceImpl implements CertService{

	@Autowired
	private UserRepository userRepository;
	

	
	@Override
	public UserCert getCert(String username, String password) throws UserNotFoundException, PasswordInvalidException {
		// 1. 是否有此人
		User user = userRepository.getUser(username);
		if(user == null) {
			throw new UserNotFoundException("查無此人"); 
		}
		// 2. 密碼驗證(BCrypt)
		boolean passwordCheck = BCrypt.checkPassword(password, user.getPasswordHash());
		
		if(!passwordCheck) {
			throw new PasswordInvalidException("密碼錯誤");
		}
		
		// 3. 簽發憑證
		UserCert userCert = new UserCert(user.getUserId(), user.getUserName(), user.getRole());
		return userCert;
	}
	
}
