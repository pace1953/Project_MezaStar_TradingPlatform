package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.exception.CertException;
import com.example.demo.model.dto.UserCert;
import com.example.demo.response.ApiResponse;
import com.example.demo.service.CertService;
import com.example.demo.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/rest")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:5174"}, allowCredentials = "true")
public class LoginRestController {
	
	@Autowired
	private CertService certService;
	@Autowired
	private UserService userService;
	
	@PostMapping("/register")
	public ResponseEntity<ApiResponse<Void>> register(@RequestParam String username, @RequestParam String password, @RequestParam String email){
		try {
			userService.registerUser(username, password, email);
			return ResponseEntity.ok(ApiResponse.success("註冊成功", null));
		} catch (Exception e) {
			return ResponseEntity
	                .status(HttpStatus.UNAUTHORIZED)
	                .body(ApiResponse.error(401, "註冊失敗: " + e.getMessage()));
		}
		
		
	}
	
	@PostMapping("/login")
	public ResponseEntity<ApiResponse<Void>> login(@RequestParam String username, @RequestParam String password, HttpSession session) {
	    try {
	        UserCert cert = certService.getCert(username, password);
	        session.setAttribute("userCert", cert);
	        return ResponseEntity.ok(ApiResponse.success("登入成功", null));
	    } catch (CertException e) {
	        return ResponseEntity
	                .status(HttpStatus.UNAUTHORIZED)
	                .body(ApiResponse.error(401, "登入失敗: " + e.getMessage()));
	    }
	}
	
	@GetMapping("/logout")
	public ResponseEntity<ApiResponse<Void>> logout(HttpSession session) {
	    if(session.getAttribute("userCert") == null) {
	    	return ResponseEntity
	                .status(HttpStatus.UNAUTHORIZED)
	                .body(ApiResponse.error(401, "登出失敗: 尚未登入 "));
	    }
	    session.invalidate(); // invalidate()：將session裡所存放的所有資訊都失效 
	    return ResponseEntity.ok(ApiResponse.success("登出成功", null));
	}
	
	@GetMapping("/check-login")
	public ResponseEntity<ApiResponse<Boolean>> checkLogin(HttpSession session) {
	    boolean loggedIn = session.getAttribute("userCert") != null;
	    return ResponseEntity.ok(ApiResponse.success("檢查登入", loggedIn));
	}
	
	@GetMapping("/user-info")
	public ResponseEntity<ApiResponse<UserCert>> getUserInfo(HttpSession session) {
	    UserCert userCert = (UserCert) session.getAttribute("userCert");
	    if (userCert != null) {
	        return ResponseEntity.ok(ApiResponse.success("獲取使用者資訊成功", userCert));
	    } else {
	        return ResponseEntity
	                .status(HttpStatus.UNAUTHORIZED)
	                .body(ApiResponse.error(401, "未登入"));
	    }
	}
	
}