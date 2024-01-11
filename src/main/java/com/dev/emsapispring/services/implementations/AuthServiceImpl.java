package com.dev.emsapispring.services.implementations;

import com.dev.emsapispring.config.security.JwtService;
import com.dev.emsapispring.entities.dtos.*;
import com.dev.emsapispring.entities.enums.UserRole;
import com.dev.emsapispring.entities.mappers.UserMapper;
import com.dev.emsapispring.entities.models.Attendee;
import com.dev.emsapispring.entities.models.User;
import com.dev.emsapispring.exceptions.CustomApiException;
import com.dev.emsapispring.exceptions.ExceptionMessage;
import com.dev.emsapispring.repositories.UserRepository;
import com.dev.emsapispring.services.interfaces.AuthService;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Override
    public UserDto register(RegisterUserDto registerUserDto) {
        userRepository.findByEmail(registerUserDto.getEmail()).ifPresent(user -> {
            throw CustomApiException.builder()
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .message(ExceptionMessage.entityAlreadyExists("User"))
                    .build();
        });

        String encodedPassword = passwordEncoder.encode(registerUserDto.getPassword());

        User user = User.builder()
                .email(registerUserDto.getEmail())
                .password(encodedPassword)
                .userRole(UserRole.USER)
                .build();

        Attendee attendee = Attendee.builder()
                .user(user)
                .firstName(registerUserDto.getFirstName())
                .lastName(registerUserDto.getLastName())
                .build();

        user.setAttendee(attendee);
        User saved = userRepository.save(user);

        return userMapper.mapToDto(saved);
    }

    @Override
    public TokenDto login(LoginUserDto loginUserDto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginUserDto.getEmail(), loginUserDto.getPassword())
        );

        User user = userRepository.findByEmail(loginUserDto.getEmail())
                .orElseThrow(() ->
                        CustomApiException.builder()
                                .httpStatus(HttpStatus.BAD_REQUEST)
                                .message(ExceptionMessage.entityNotFound("User"))
                                .build()
                );

        String token = jwtService.generateToken(user);

        return TokenDto.builder()
                .token(token)
                .build();
    }

    @Override
    public TokenValidationResponseDto validateToken(TokenDto tokenDto) {
        try {
            jwtService.validateToken(tokenDto.getToken());
            return TokenValidationResponseDto.builder()
                    .isValid(true)
                    .build();
        } catch (ExpiredJwtException e) {
            throw CustomApiException.builder()
                    .httpStatus(HttpStatus.UNAUTHORIZED)
                    .message("Token expired")
                    .build();
        } catch (MalformedJwtException e) {
            throw CustomApiException.builder()
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .message("Invalid token format")
                    .build();
        } catch (JwtException e) {
            throw CustomApiException.builder()
                    .httpStatus(HttpStatus.UNAUTHORIZED)
                    .message("Invalid token")
                    .build();
        }
    }

}
