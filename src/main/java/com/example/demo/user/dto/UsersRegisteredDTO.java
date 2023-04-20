package com.example.demo.user.dto;

import com.example.demo.user.model.Gender;
import lombok.Data;

import java.util.Date;

@Data
public class UsersRegisteredDTO {

	
    private String fullname;
	private String username;
	private String email;
	private String password;
	private String phoneNum;
	private String avatarLink;
	String role;

//	private byte[] profileImg;


	
	
}
