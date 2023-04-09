package com.example.demo.user.repository;

import com.example.demo.user.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {
    Users findByEmail(String email);
    Users findUsersByPhoneNum(String phoneNum);
}
