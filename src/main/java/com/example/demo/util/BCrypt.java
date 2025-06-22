package com.example.demo.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

// 使用 BCrypt演算法 加密
public class BCrypt {
	
	// (12)是Work Factor的參數，為2的12次方 -> 進行4096次的計算(密碼加密)
	private static final BCryptPasswordEncoder ENCODER = new BCryptPasswordEncoder(12);
	
	// 加密密碼
	public static String getBCrypt(String rawPassword) {
		return ENCODER.encode(rawPassword);
	}
	
	// 驗證密碼(
	public static boolean checkPassword(String rawPassword, String BCryptPassword) {
		return ENCODER.matches(rawPassword, BCryptPassword);
	}
	
	// 檢查密碼是否為 BCrypt 格式
	public static boolean isBCryptFormat(String password) {
		if(password == null || password.length() != 60) {
			return false;
		}
		return password.matches("^\\$2[abxy]\\$\\d{2}\\$[A-Za-z0-9./]{53}$");
	}
	
}
