package com.example.demo.user.service;

import com.example.demo.user.dto.CommonResponse;
import com.example.demo.user.dto.UsersRegisteredDTO;
import com.example.demo.user.model.Users;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserJwtService extends UserDetailsService {
    Users save(UsersRegisteredDTO userRegisteredDTO);

    Users save( String phoneNum);
    Users findUserByUsername(String username);

    CommonResponse updateUser(String fullname, String email, String phoneNum, long id);
    CommonResponse deleteUser(long id);
}
