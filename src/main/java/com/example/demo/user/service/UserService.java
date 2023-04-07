package com.example.demo.user.service;

import com.example.demo.user.dto.UsersRegisteredDTO;
import com.example.demo.user.model.Users;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    Users save(UsersRegisteredDTO userRegisteredDTO);

}
