package com.example.demo.user.controller;

import com.example.demo.user.dto.UsersRegisteredDTO;
import com.example.demo.user.service.UserJwtService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/registration")
public class RegistrationController {

	 private UserJwtService userJwtService;

	    public RegistrationController(UserJwtService userJwtService) {
	        super();
	        this.userJwtService = userJwtService;
	    }

	    @ModelAttribute("user")
	    public UsersRegisteredDTO userRegistrationDto() {
	        return new UsersRegisteredDTO();
	    }

	    @GetMapping
	    public String showRegistrationForm() {
	        return "register";
	    }

	    @PostMapping
	    public String registerUserAccount(@ModelAttribute("user")
                                                  UsersRegisteredDTO registrationDto) {
//	        userService.save(registrationDto);
	        return "redirect:/login";
	    }
}
