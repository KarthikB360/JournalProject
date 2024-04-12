package com.user.controller;

import com.user.entity.AuthRequest;
import com.user.entity.TokenRequest;
import com.user.exception.CustomException;
import com.user.exception.CustomNotFoundException;
import com.user.jwt.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/generateToken")
    public String authenticateAndGetToken(@RequestBody AuthRequest authRequest) throws CustomException {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUserName(), authRequest.getPassword()));
        if (authentication.isAuthenticated()) {
            return jwtService.generateToken(authRequest.getUserName());
        } else {
            throw new CustomNotFoundException("invalid user request!");
        }
    }

    @PostMapping("/validateToken")
    public Boolean validateToken(@RequestBody TokenRequest tokenRequest) throws CustomException {
        return jwtService.validateToken(tokenRequest.getToken(), tokenRequest.getRole());
    }
}
