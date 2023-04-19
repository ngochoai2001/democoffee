package com.example.demo.user.controller;

import com.example.demo.sms.OTPDto;
import com.example.demo.sms.OTPSender;
import com.example.demo.user.dto.UserLoginDTO;
import com.example.demo.user.repository.UserRepository;
import com.example.demo.user.service.UserJwtService;
import com.example.demo.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.ExecutionException;


@RestController
@RequestMapping("/login")

public class LoginController {
	@Autowired
	private UserJwtService userJwtService;
	@Autowired
	private JwtUtils jwtUtils;
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
	public String validateOTP(@RequestBody OTPDto otpDto) throws ExecutionException {
		if(otpSender.validateOTP(otpDto)){
			try{
				userJwtService.loadUserByUsername(otpDto.getPhoneNum());
			}catch(UsernameNotFoundException e){
				userJwtService.save(otpDto.getPhoneNum());
			}
			return jwtUtils.generateToken(otpDto.getPhoneNum());

		}
		return "Invalid OTP";

	}
	
}
