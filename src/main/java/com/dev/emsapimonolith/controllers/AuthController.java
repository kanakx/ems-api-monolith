package com.dev.emsapimonolith.controllers;

import com.dev.emsapimonolith.entities.dtos.*;
import com.dev.emsapimonolith.services.interfaces.AuthService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

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

    @PutMapping("/change-password/{idUser}")
    public void changePassword(@PathVariable Long idUser, @RequestBody PasswordChangeDto passwordChangeDto) {
        logger.info("Received password change request for user with ID: {}", idUser);
        authService.changePassword(idUser, passwordChangeDto);
        logger.info("Password change request completed for user with ID: {}", idUser);
    }

    @PostMapping("/validate")
    public TokenValidationResponseDto validateToken(@RequestBody TokenDto tokenDto) {
        logger.info("Received token validation request");
        TokenValidationResponseDto validationResponse = authService.validateToken(tokenDto);
        logger.info("Token validation request completed");
        return validationResponse;
    }

}
