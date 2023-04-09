package com.example.demo.user.controller;

import com.example.demo.sms.OTPDto;
import com.example.demo.sms.OTPSender;
import com.example.demo.user.dto.UserLoginDTO;
import com.example.demo.user.model.Users;
import com.example.demo.user.repository.UserRepository;
import com.example.demo.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;


@RestController
@RequestMapping("/login")

public class LoginController {
	@Autowired
	private UserService userService;
	@Autowired
	private OTPSender otpSender;
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
//	@PostMapping
//	public void loginUser(@RequestBody  UserLoginDTO userLoginDTO) {
//		System.out.println("UserDTO"+userLoginDTO);
//		 userService.loadUserByUsername(userLoginDTO.getUsername());
//
//
//	}
	@GetMapping("/facebook")
	public void loginWithFacebook(HttpServletResponse response) throws IOException {
		response.sendRedirect("/oauth2/authorization/facebook");
	}


	@PostMapping("/phonenum")
	public OTPDto loginWithPhoneNum(@RequestBody OTPDto otpDto){
		return otpSender.sendOTP(otpDto.getPhoneNum());
	}
	@PostMapping()
	public void validateOTP(@RequestBody OTPDto otpDto){
		if(otpSender.validateOTP(otpDto)){
			try{
				userService.loadUserByUsername(otpDto.getPhoneNum());
			}catch(UsernameNotFoundException e){
				userService.save(otpDto.getPhoneNum());
			}
		}

	}
	
}
