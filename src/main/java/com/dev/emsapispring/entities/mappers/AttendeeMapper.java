package com.dev.emsapispring.entities.mappers;

import com.ems.emsdataservicespring.entities.dtos.AttendeeDto;
import com.ems.emsdataservicespring.entities.models.Attendee;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",
        uses = {
                AttendeeEventMapper.class
        },
        builder = @Builder(disableBuilder = true)
)
public interface AttendeeMapper {

    @Mapping(source = "attendeeEventList", target = "attendeeEventDtoList")
    AttendeeDto mapToDto(Attendee attendee);

}
