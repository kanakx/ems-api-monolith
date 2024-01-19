package com.dev.emsapimonolith.services.interfaces;


import com.dev.emsapimonolith.entities.dtos.AttendeeEventAssociationDto;

import java.util.List;

public interface AttendeeEventService {

    List<AttendeeEventAssociationDto> findAll();
    AttendeeEventAssociationDto update(Long id, AttendeeEventAssociationDto attendeeEventAssociationDto);
    void delete(AttendeeEventAssociationDto attendeeEventAssociationDto);

}
