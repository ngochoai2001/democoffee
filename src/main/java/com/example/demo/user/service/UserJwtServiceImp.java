package com.example.demo.user.service;

import com.amazonaws.services.connect.model.UserNotFoundException;
import com.example.demo.user.dto.CommonResponse;
import com.example.demo.user.dto.UsersRegisteredDTO;
import com.example.demo.user.model.Role;
import com.example.demo.user.model.SaveAccount;
import com.example.demo.user.model.Users;
import com.example.demo.user.repository.RoleRepository;
import com.example.demo.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserJwtServiceImp implements UserJwtService {
    @Autowired
    private UserRepository userRepo;

    @Autowired
    private RoleRepository roleRepo;


    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Users user = userRepo.findUsersByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("Invalid username or password.");
        }
        SaveAccount.users = user;
        if (user.getEmail() == null && user.getPassword() == null)
            return new User(username, "", mapRolesToAuthorities(user.getRole()));
        return new org.springframework.security.core.userdetails.User(user.getEmail(), "", mapRolesToAuthorities(user.getRole()));
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Set<Role> roles) {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getRole())).collect(Collectors.toList());
    }

    @Override
    public Users save(UsersRegisteredDTO userRegisteredDTO) {
        com.example.demo.user.model.Role role = roleRepo.findByRole("USER");
        Users user = userRepo.findUsersByUsername(userRegisteredDTO.getUsername());
        try {
            loadUserByUsername(userRegisteredDTO.getUsername());
        } catch (UsernameNotFoundException e) {
            user = new Users();
            user.setUsername(userRegisteredDTO.getEmail());
            user.setEmail(userRegisteredDTO.getEmail());
            user.setFullname(userRegisteredDTO.getFullname());
            if (user.getPassword() != null) user.setPassword(passwordEncoder.encode(userRegisteredDTO.getPassword()));
            user.setRole(role);

            return userRepo.save(user);
        }
        return user;
    }

    @Override
    public Users save(String phoneNum) {
        com.example.demo.user.model.Role role = roleRepo.findByRole("USER");
        Users user = new Users();
        user.setUsername(phoneNum);
        user.setPhoneNum(phoneNum);
        user.setRole(role);
        return userRepo.save(user);
    }

    @Override
    public Users findUserByUsername(String username) {
        return userRepo.findUsersByUsername(username);
    }


    @Override
    @Transactional
    public CommonResponse updateUser(String fullname, String email, String phoneNum, long id) {
        try {
            int result = userRepo.updateUser(fullname, email, phoneNum, id);
            if (result == 1) {
                SaveAccount.users.setFullname(fullname);
                SaveAccount.users.setEmail(email);
                return new CommonResponse("successful", 200);
            }
            return new CommonResponse("failure", 400);
        } catch (Exception e) {
            return new CommonResponse("failure", 400);
        }
    }

    @Override
    @Transactional
    public CommonResponse deleteUser(long id) {
        try {
            int result = userRepo.deleteUser(id);
            if (result == 1) {
                SaveAccount.users = new Users();
                return new CommonResponse("successful", 200);
            }
            return new CommonResponse("failure", 400);
        } catch (Exception e) {
            return new CommonResponse("failure", 400);
        }
    }
}
