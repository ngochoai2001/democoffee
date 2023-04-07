package com.example.demo.user.dto;

import com.example.demo.user.model.Gender;
import lombok.Data;

import java.util.Date;

@Data
public class UsersRegisteredDTO {

	
    private String fullname;
	private String email;
	private String password;
	private String phoneNum;
	String role;

//	private byte[] profileImg;


	
	
}
