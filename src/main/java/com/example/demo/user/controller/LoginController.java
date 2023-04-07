package com.example.demo.user.controller;

import com.example.demo.user.dto.UserLoginDTO;
import com.example.demo.user.model.Users;
import com.example.demo.user.repository.UserRepository;
import com.example.demo.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@RestController
@RequestMapping("/login")
public class LoginController {
	@Autowired
	private UserService userService;
	
	@Autowired
	UserRepository userRepo;
    
    @ModelAttribute("user")
    public UserLoginDTO userLoginDTO() {
        return new UserLoginDTO();
    }
    
	@GetMapping
	public String login() {
		return "login";
	}
	@GetMapping("/google")
	private void loginWithGoogle(HttpServletResponse response) throws IOException {
		response.sendRedirect("/oauth2/authorization/google");
	}
	@PostMapping
	public void loginUser(@RequestBody  UserLoginDTO userLoginDTO) {
		System.out.println("UserDTO"+userLoginDTO);
		 userService.loadUserByUsername(userLoginDTO.getUsername());


	}
	
}
