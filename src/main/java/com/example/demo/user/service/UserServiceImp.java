package com.example.demo.user.service;
import com.example.demo.user.dto.UsersRegisteredDTO;
import com.example.demo.user.model.Role;
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

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImp implements UserService {
    @Autowired
    private UserRepository userRepo;

    @Autowired
    private RoleRepository roleRepo;


    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();



    @Override
    public UserDetails loadUserByUsername(String phoneNum) throws UsernameNotFoundException {

        Users user = userRepo.findUsersByPhoneNum(phoneNum);
        if(user == null) {
            throw new UsernameNotFoundException("Invalid username or password.");
        }
        if(user.getEmail()==null && user.getPassword()==null)
            return new User(phoneNum, "",mapRolesToAuthorities(user.getRole()));
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), mapRolesToAuthorities(user.getRole()));
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Set<Role> roles){
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getRole())).collect(Collectors.toList());
    }

    @Override
    public Users save(UsersRegisteredDTO userRegisteredDTO) {
        com.example.demo.user.model.Role role = roleRepo.findByRole("USER");

        Users user = new Users();
        user.setEmail(userRegisteredDTO.getEmail());
        user.setFullname(userRegisteredDTO.getFullname());
        user.setPassword(passwordEncoder.encode(userRegisteredDTO.getPassword()));
        user.setRole(role);

        return userRepo.save(user);
    }

    @Override
    public Users save(String phoneNum) {
        com.example.demo.user.model.Role role = roleRepo.findByRole("USER");

        Users user = new Users();
        user.setPhoneNum(phoneNum);
        user.setRole(role);
        return userRepo.save(user);
    }


}
