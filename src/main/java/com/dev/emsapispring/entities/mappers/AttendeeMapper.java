package com.dev.emsapispring.entities.mappers;

import com.dev.emsapispring.entities.dtos.AttendeeDto;
import com.dev.emsapispring.entities.models.Attendee;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",
        uses = {
                AttendeeEventMapper.class,
                UserMapper.class
        },
        builder = @Builder(disableBuilder = true)
)
public interface AttendeeMapper {

    @Mapping(source = "attendeeEventList", target = "attendeeEventDtoList")
    @Mapping(source = "user", target = "userDto")
    AttendeeDto mapToDto(Attendee attendee);

}
