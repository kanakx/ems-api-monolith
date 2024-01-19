package com.dev.emsapimonolith.entities.mappers;

import com.dev.emsapimonolith.entities.dtos.UserDto;
import com.dev.emsapimonolith.entities.models.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto mapToDto(User user);

}
