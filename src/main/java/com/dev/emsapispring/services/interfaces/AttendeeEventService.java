package com.dev.emsapispring.services.interfaces;


import com.dev.emsapispring.entities.dtos.AttendeeEventAssociationDto;

import java.util.List;

public interface AttendeeEventService {

    List<AttendeeEventAssociationDto> findAll();
    AttendeeEventAssociationDto update(Long id, AttendeeEventAssociationDto attendeeEventAssociationDto);
    void delete(AttendeeEventAssociationDto attendeeEventAssociationDto);

}
