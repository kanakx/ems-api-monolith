package com.dev.emsapimonolith.services.implementations;

import com.dev.emsapimonolith.config.security.JwtService;
import com.dev.emsapimonolith.entities.dtos.*;
import com.dev.emsapimonolith.entities.enums.UserRole;
import com.dev.emsapimonolith.entities.mappers.UserMapper;
import com.dev.emsapimonolith.entities.models.User;
import com.dev.emsapimonolith.exceptions.CustomApiException;
import com.dev.emsapimonolith.repositories.UserRepository;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    UserRepository userRepository;

    @Mock
    JwtService jwtService;

    @Mock
    AuthenticationManager authenticationManager;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    UserMapper userMapper;

    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    void register_Success() {
        // Given
        RegisterUserDto registerUserDto = RegisterUserDto.builder()
                .email("test@test.com")
                .password("pwd")
                .build();

        User user = User.builder()
                .email(registerUserDto.getEmail())
                .password(registerUserDto.getPassword())
                .userRole(UserRole.USER)
                .build();

        UserDto userDto = UserDto.builder()
                .email(user.getEmail())
                .userRole(user.getUserRole())
                .build();

        when(userRepository.findByEmail(registerUserDto.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(registerUserDto.getPassword())).thenReturn("encodedPwd");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.mapToDto(user)).thenReturn(userDto);

        // When
        UserDto serviceResult = authService.register(registerUserDto);

        // Then
        assertEquals(registerUserDto.getEmail(), serviceResult.getEmail());
        verify(userRepository, times(1)).findByEmail(registerUserDto.getEmail());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void register_Failure_UserAlreadyExists() {
        // Given
        RegisterUserDto registerUserDto = RegisterUserDto.builder()
                .email("test@test.com")
                .password("pwd")
                .build();

        User user = User.builder()
                .email(registerUserDto.getEmail())
                .password(registerUserDto.getPassword())
                .userRole(UserRole.USER)
                .build();

        when(userRepository.findByEmail(registerUserDto.getEmail())).thenReturn(Optional.of(user));

        // When
        // Then
        assertThrows(CustomApiException.class, () -> authService.register(registerUserDto));
        verify(userRepository, times(1)).findByEmail(registerUserDto.getEmail());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void login_Success() {
        // Given
        LoginUserDto loginUserDto = LoginUserDto.builder()
                .email("test@test.com")
                .password("pwd")
                .build();

        User user = User.builder()
                .email(loginUserDto.getEmail())
                .password(loginUserDto.getPassword())
                .userRole(UserRole.USER)
                .build();

        String token = "token";

        when(authenticationManager.authenticate(any(Authentication.class))).thenReturn(null);
        when(userRepository.findByEmail(loginUserDto.getEmail())).thenReturn(Optional.of(user));
        when(jwtService.generateToken(user)).thenReturn(token);

        // When
        TokenDto serviceResult = authService.login(loginUserDto);

        // Then
        assertEquals(token, serviceResult.getToken());
        verify(authenticationManager, times(1)).authenticate(any(Authentication.class));
        verify(userRepository, times(1)).findByEmail(loginUserDto.getEmail());
        verify(jwtService, times(1)).generateToken(user);
    }

    @Test
    void login_Failure_UserNotFound() {
        // Given
        LoginUserDto loginUserDto = LoginUserDto.builder()
                .email("test@test.com")
                .password("pwd")
                .build();

        when(userRepository.findByEmail(loginUserDto.getEmail())).thenReturn(Optional.empty());

        // When
        // Then
        assertThrows(CustomApiException.class, () -> authService.login(loginUserDto));
        verify(userRepository, times(1)).findByEmail(loginUserDto.getEmail());
        verify(jwtService, never()).generateToken(any(User.class));
    }

    @Test
    void changePassword_Success() {
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getName()).thenReturn("1");

        Long userId = 1L;
        PasswordChangeDto passwordChangeDto = new PasswordChangeDto("currentPassword", "newPassword");
        User user = User.builder()
                .idUser(userId)
//                .email()
                .password("encodedCurrentPassword")
                .userRole(UserRole.USER)
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(passwordChangeDto.getCurrentPassword(), user.getPassword())).thenReturn(true);

        // When
        authService.changePassword(userId, passwordChangeDto);

        // Then
        verify(userRepository).save(any(User.class));
        verify(passwordEncoder).encode(passwordChangeDto.getNewPassword());
    }

    @Test
    void changePassword_Failure_UserNotFound() {
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getName()).thenReturn("1");

        Long userId = 1L;
        PasswordChangeDto passwordChangeDto = new PasswordChangeDto("currentPassword", "newPassword");

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // When
        // Then
        assertThrows(CustomApiException.class, () -> authService.changePassword(userId, passwordChangeDto));
    }

    @Test
    void changePassword_Failure_Unauthorized() {
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getName()).thenReturn("2");

        Long userId = 1L, loggedInUserId = 2L;
        PasswordChangeDto passwordChangeDto = new PasswordChangeDto("currentPassword", "newPassword");
        User user = new User();
        user.setIdUser(loggedInUserId);

        when(userRepository.findById(loggedInUserId)).thenReturn(Optional.of(user));

        // When
        // Then
        assertThrows(CustomApiException.class, () -> authService.changePassword(userId, passwordChangeDto));
    }

    @Test
    void changePassword_Failure_IncorrectCurrentPassword() {
        // Local setup for SecurityContext and Authentication
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getName()).thenReturn("1");

        Long userId = 1L;
        PasswordChangeDto passwordChangeDto = new PasswordChangeDto("currentPassword", "newPassword");
        User user = new User();
        user.setIdUser(userId);
        user.setPassword("encodedCurrentPassword");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(passwordChangeDto.getCurrentPassword(), user.getPassword())).thenReturn(false);

        // When
        // Then
        assertThrows(CustomApiException.class, () -> authService.changePassword(userId, passwordChangeDto));
    }


    @Test
    void validateToken_Success() {
        // Given
        String validToken = "token";
        TokenDto validTokenDto = TokenDto
                .builder()
                .token(validToken)
                .build();

        // When
        TokenValidationResponseDto serviceResult = authService.validateToken(validTokenDto);

        // Then
        assertTrue(serviceResult.getIsValid());
        verify(jwtService, times(1)).validateToken(validToken);
    }

    @Test
    void validateToken_Failure_ExpiredJwtException() {
        // Given
        String expiredToken = "token";
        TokenDto tokenDto = TokenDto
                .builder()
                .token(expiredToken)
                .build();

        doThrow(new ExpiredJwtException(null, null, "Token expired")).when(jwtService).validateToken(expiredToken);

        // When
        // Then
        assertThrows(CustomApiException.class, () -> authService.validateToken(tokenDto));
        verify(jwtService, times(1)).validateToken(expiredToken);
    }

    @Test
    void validateToken_Failure_MalformedJwtException() {
        // Given
        String invalidToken = "token";
        TokenDto tokenDto = TokenDto
                .builder()
                .token(invalidToken)
                .build();

        doThrow(new MalformedJwtException("Invalid token format")).when(jwtService).validateToken(invalidToken);

        // When
        // Then
        assertThrows(CustomApiException.class, () -> authService.validateToken(tokenDto));
        verify(jwtService, times(1)).validateToken(invalidToken);
    }

    @Test
    void validateToken_Failure_JwtException() {
        // Given
        String invalidToken = "token";
        TokenDto tokenDto = TokenDto
                .builder()
                .token(invalidToken)
                .build();

        doThrow(new JwtException("Invalid token")).when(jwtService).validateToken(invalidToken);

        // When
        // Then
        assertThrows(CustomApiException.class, () -> authService.validateToken(tokenDto));
        verify(jwtService, times(1)).validateToken(invalidToken);
    }

}
