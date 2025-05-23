package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.demo.service.UserService;

@SpringBootTest
public class UserJPARead {
	
	@Autowired
	private UserService userService;
	
	@Test
	public void testUserAdd() {
		System.out.println(userService.getUser("Sheng"));
		System.out.println(userService.getUser("Pace"));
		System.out.println(userService.getUser("Box"));
		System.out.println(userService.getUser("jack")); // null
	}
	
}