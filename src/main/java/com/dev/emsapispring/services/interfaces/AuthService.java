package com.dev.emsapispring.services.interfaces;

import com.dev.emsapispring.entities.dtos.*;

public interface AuthService {

    UserDto register(RegisterUserDto registerUserDto);
    TokenDto login(LoginUserDto loginUserDto);
    void changePassword(Long idUser, PasswordChangeDto passwordChangeDto);
    TokenValidationResponseDto validateToken(TokenDto tokenDto);

}
