package com.dev.emsapispring.entities.mappers;

import com.ems.emsdataservicespring.entities.dtos.AddEventDto;
import com.ems.emsdataservicespring.entities.dtos.EventDto;
import com.ems.emsdataservicespring.entities.models.Event;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EventMapper {

    EventDto mapToDto(Event event);

    @Mapping(target = "idEvent", ignore = true)
    Event mapToEntity(AddEventDto addEventDto);

}
