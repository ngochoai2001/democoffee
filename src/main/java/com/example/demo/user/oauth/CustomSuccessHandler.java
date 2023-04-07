package com.example.demo.user.oauth;

import com.example.demo.user.dto.UsersRegisteredDTO;
import com.example.demo.user.repository.UserRepository;
import com.example.demo.user.service.UserService;

import com.google.gson.Gson;
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
    UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        String redirectUrl = null;
        UsersRegisteredDTO user = null;
        if (authentication.getPrincipal() instanceof DefaultOAuth2User) {
            DefaultOAuth2User userDetails = (DefaultOAuth2User) authentication.getPrincipal();
            String email = userDetails.getAttribute("email") != null ? userDetails.getAttribute("email") : userDetails.getAttribute("login") + "@gmail.com";

            if (userRepo.findByEmail(email) == null) {
                user = new UsersRegisteredDTO();
                user.setEmail(email);
                user.setFullname(userDetails.getAttribute("given_name") != null ? userDetails.getAttribute("given_name") : userDetails.getAttribute("login"));
                user.setPassword(("Dummy"));
                user.setRole("USER");           
//                user.setProfileImg(ImageUtils.downloadImgFromGGLink(userDetails.getAttribute("picture")));
                userService.save(user);
            }
        }
        if (user != null) {
            response.setStatus(HttpServletResponse.SC_OK);
            Gson gson = new Gson();
            response.getWriter().append(gson.toJson(user));

        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().append("Faild to login, pls try again ");
        }
        response.setCharacterEncoding("UTF-8");

//		redirectUrl = "/dashboard";
//		new DefaultRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }

}
