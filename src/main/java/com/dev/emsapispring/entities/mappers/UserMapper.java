package com.dev.emsapispring.entities.mappers;

import com.dev.emsapispring.entities.dtos.UserDto;
import com.dev.emsapispring.entities.models.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto mapToDto(User user);

}
