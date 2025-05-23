package com.example.demo.model.dto;

import lombok.Data;

@Data
public class UserDto {
	private Integer userId;
	private String userName;
	private String email;
	private String role;
	private Boolean active;
}
