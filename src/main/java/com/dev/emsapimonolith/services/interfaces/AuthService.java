package com.dev.emsapimonolith.services.interfaces;

import com.dev.emsapimonolith.entities.dtos.*;

public interface AuthService {

    UserDto register(RegisterUserDto registerUserDto);
    TokenDto login(LoginUserDto loginUserDto);
    void changePassword(Long idUser, PasswordChangeDto passwordChangeDto);
    TokenValidationResponseDto validateToken(TokenDto tokenDto);

}
