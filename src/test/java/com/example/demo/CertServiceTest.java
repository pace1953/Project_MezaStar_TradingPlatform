package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.demo.exception.CertException;
import com.example.demo.service.CertService;
import com.example.demo.service.UserService;

@SpringBootTest
public class CertServiceTest {
	
	@Autowired
	private CertService certService;
	
	@Test
	public void testUserAdd() throws CertException {
		try {
			System.out.println(certService.getCert("Sheng", "0905"));
		} catch (CertException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		
	}
	
}