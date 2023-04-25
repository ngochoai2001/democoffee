package com.example.demo.user.oauth;

import com.example.demo.user.dto.UserLoginResponse;
import com.example.demo.user.dto.UsersRegisteredDTO;
import com.example.demo.user.model.SaveAccount;
import com.example.demo.user.repository.UserRepository;
import com.example.demo.user.service.UserJwtService;

import com.example.demo.utils.JwtUtils;
import com.google.gson.Gson;
import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    UserRepository userRepo;

    @Autowired
    UserJwtService userJwtService;

    @Autowired
    JwtUtils jwtUtils;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        String redirectUrl = null;
        UsersRegisteredDTO user = null;
        String accessToken = null;
        //oauth 2
        if (authentication.getPrincipal() instanceof DefaultOAuth2User) {
            DefaultOAuth2User userDetails = (DefaultOAuth2User) authentication.getPrincipal();
            String email = userDetails.getAttribute("email") != null ? userDetails.getAttribute("email") : userDetails.getAttribute("login") + "@gmail.com";

            if (userRepo.findByEmail(email) == null) {
                user = new UsersRegisteredDTO();
                user.setEmail(email);
//                user.setAvatarLink();
                user.setFullname(userDetails.getAttribute("given_name") != null ? userDetails.getAttribute("given_name") : userDetails.getAttribute("login"));
                user.setPassword(("Dummy"));
                user.setRole("USER");
//                user.setProfileImg(ImageUtils.downlo  adImgFromGGLink(userDetails.getAttribute("picture")));
                SaveAccount.users = userJwtService.save(user);
            }
            accessToken = jwtUtils.generateToken(email);
        }
        UserLoginResponse userLoginResponse = new UserLoginResponse();
        userLoginResponse.setAccessToken(accessToken);
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
        Gson gson = new Gson();
        response.getWriter().append(gson.toJson(userLoginResponse));
        response.setCharacterEncoding("UTF-8");

//		redirectUrl = "/dashboard";
//		new DefaultRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }

}
