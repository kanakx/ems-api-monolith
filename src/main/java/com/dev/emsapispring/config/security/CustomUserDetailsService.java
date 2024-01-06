package com.dev.emsapispring.config.security;

import com.dev.emsapispring.entities.models.User;
import com.dev.emsapispring.exceptions.CustomApiException;
import com.dev.emsapispring.exceptions.ExceptionMessage;
import com.dev.emsapispring.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findByEmail(username);
        return userOptional
                .orElseThrow(() -> CustomApiException.builder()
                        .httpStatus(HttpStatus.NOT_FOUND)
                        .message(ExceptionMessage.entityNotFound("User"))
                        .build()
                );
    }

}
