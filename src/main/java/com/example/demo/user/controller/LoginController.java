package com.example.demo.user.controller;

import com.example.demo.sms.OTPDto;
import com.example.demo.sms.OTPSender;
import com.example.demo.user.dto.*;
import com.example.demo.user.model.SaveAccount;
import com.example.demo.user.model.Users;
import com.example.demo.user.repository.UserRepository;
import com.example.demo.user.service.UserJwtService;
import com.example.demo.utils.JwtUtils;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.concurrent.ExecutionException;


@RestController
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
    public UserGGLoginRequest userLoginDTO() {
        return new UserGGLoginRequest();
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/login/google")
    private UserLoginResponse loginWithGoogle(@Autowired NetHttpTransport transport, @Autowired GsonFactory factory, @RequestBody UserGGLoginRequest request) throws IOException, GeneralSecurityException {

        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, factory)
                // Specify the CLIENT_ID of the app that accesses the backend:
                .setAudience(Collections.singletonList(JwtUtils.GOOGLE_CLIENT_ID))
                // Or, if multiple clients access the backend:
                .build();

        GoogleIdToken idToken = verifier.verify(request.getGoogleToken());
        UserLoginResponse userLoginResponse = new UserLoginResponse();

        if (idToken != null) {
            GoogleIdToken.Payload payload = idToken.getPayload();

            // Print user identifier
            String userId = payload.getSubject();
            System.out.println("User ID: " + userId);
            // Get profile information from payload
            boolean emailVerified = Boolean.valueOf(payload.getEmailVerified());
            String pictureUrl = (String) payload.get("picture");
            if (emailVerified) {
                UsersRegisteredDTO dto = new UsersRegisteredDTO();
                dto.setFullname((String) payload.get("name"));
                dto.setEmail((String) payload.getEmail());
                dto.setUsername(payload.getEmail());
                SaveAccount.users = userJwtService.save(dto);
                String accessToken = jwtUtils.generateToken(payload.getEmail());
                userLoginResponse.setAccessToken(accessToken);
                userLoginResponse.setStatus(200);

            } else {
                userLoginResponse.setStatus(400);
                userLoginResponse.setMessage("Unverified Token");
            }

        } else {
            userLoginResponse.setStatus(400);
            userLoginResponse.setMessage("Invalid ID Token");
        }

        return userLoginResponse;
    }

    @GetMapping("/login/facebook")
    public void loginWithFacebook(HttpServletResponse response) throws IOException {
        response.sendRedirect("/oauth2/authorization/facebook");
    }

    @PostMapping("/login/phonenum")
    public OTPDto loginWithPhoneNum(@RequestBody OTPDto otpDto) {
        return otpSender.sendOTP(otpDto.getPhoneNum());
    }

    @PostMapping("/login")
    public UserLoginResponse validateOTP(@RequestBody OTPDto otpDto, HttpServletResponse response) throws ExecutionException {
        UserLoginResponse userLoginResponse = new UserLoginResponse();
        if (otpSender.validateOTP(otpDto)) {
            try {
                userJwtService.loadUserByUsername(otpDto.getPhoneNum());
            } catch (UsernameNotFoundException e) {
                SaveAccount.users = userJwtService.save(otpDto.getPhoneNum());
            }
            userLoginResponse.setAccessToken(jwtUtils.generateToken(otpDto.getPhoneNum()));
            userLoginResponse.setStatus(200);
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            userLoginResponse.setStatus(400);
            userLoginResponse.setMessage("Invalid OTP");
        }

        return userLoginResponse;
    }

    @GetMapping("/user")
    public Users getCurrentUser() {
        return SaveAccount.users;
    }

    @PatchMapping("/user/update/{id}")
    public CommonResponse updateUser(@PathVariable long id, @RequestBody UpdateUserRequest updateUserRequest) {
        return userJwtService.updateUser(updateUserRequest.getFullname(), updateUserRequest.getEmail(), updateUserRequest.getPhoneNum(), id);
    }

    @DeleteMapping("/user/delete/{id}")
    public CommonResponse deleteUser(@PathVariable long id) {
        return userJwtService.deleteUser(id);
    }
}
