package com.dev.emsapimonolith.entities.mappers;

import com.dev.emsapimonolith.entities.dtos.AttendeeEventAssociationDto;
import com.dev.emsapimonolith.entities.dtos.AttendeeEventDto;
import com.dev.emsapimonolith.entities.models.AttendeeEvent;
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

    @Mapping(source = "attendee.idAttendee", target = "idAttendee")
    @Mapping(source = "event.idEvent", target = "idEvent")
    AttendeeEventAssociationDto mapToAssociationDto(AttendeeEvent attendeeEvent);

}
