package com.dev.emsapimonolith.entities.mappers;

import com.dev.emsapimonolith.entities.dtos.AddEventDto;
import com.dev.emsapimonolith.entities.dtos.EventDto;
import com.dev.emsapimonolith.entities.models.Event;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EventMapper {

    EventDto mapToDto(Event event);

    @Mapping(target = "idEvent", ignore = true)
    Event mapToEntity(AddEventDto addEventDto);

}
