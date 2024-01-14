package com.dev.emsapispring.controllers;

import com.dev.emsapispring.entities.dtos.*;
import com.dev.emsapispring.services.interfaces.AuthService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private final AuthService authService;

    @PostMapping("/register")
    public UserDto register(@RequestBody RegisterUserDto registerUserDto) {
        logger.info("Received registration request for email: {}", registerUserDto.getEmail());
        UserDto registeredUser = authService.register(registerUserDto);
        logger.info("Registration request completed for email: {}", registeredUser.getEmail());
        return registeredUser;
    }

    @PostMapping("/login")
    public TokenDto login(@RequestBody LoginUserDto loginUserDto) {
        logger.info("Received login request for email: {}", loginUserDto.getEmail());
        TokenDto token = authService.login(loginUserDto);
        logger.info("Login request completed for email: {}", loginUserDto.getEmail());
        return token;
    }

    @PostMapping("/validate")
    public TokenValidationResponseDto validateToken(@RequestBody TokenDto tokenDto) {
        logger.debug("Received token validation request");
        TokenValidationResponseDto validationResponse = authService.validateToken(tokenDto);
        logger.debug("Token validation request completed");
        return validationResponse;
    }

}
