package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.demo.service.UserService;

@SpringBootTest
public class UserJPATest {

	@Autowired
	private UserService userService;
	
	@Test
	public void testuserAdd() {
		userService.addUser("Sheng", "0905", "sheng0905@gmail.com", true, "admin");
		userService.addUser("Pace", "1124", "Pace1124@gmail.com", true, "admin");
		userService.addUser("Box", "1019", "Box1019@gmail.com", true, "admin");
	}
}
