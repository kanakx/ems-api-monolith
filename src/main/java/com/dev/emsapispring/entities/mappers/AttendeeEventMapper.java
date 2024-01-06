package com.dev.emsapispring.entities.mappers;

import com.dev.emsapispring.entities.dtos.AttendeeEventDto;
import com.dev.emsapispring.entities.models.AttendeeEvent;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",
        uses = {
                EventMapper.class
        },
        builder = @Builder(disableBuilder = true)
)
public interface AttendeeEventMapper {

    @Mapping(source = "event", target = "eventDto")
    AttendeeEventDto mapToDto(AttendeeEvent attendeeEvent);

}
