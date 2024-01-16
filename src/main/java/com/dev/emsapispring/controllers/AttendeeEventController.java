package com.dev.emsapispring.controllers;

import com.dev.emsapispring.entities.dtos.AttendeeEventAssociationDto;
import com.dev.emsapispring.services.interfaces.AttendeeEventService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/attendeesEvents")
@RequiredArgsConstructor
public class AttendeeEventController {

    private static final Logger logger = LoggerFactory.getLogger(AttendeeEventController.class);
    private final AttendeeEventService attendeeEventService;

    @GetMapping
    public List<AttendeeEventAssociationDto> findAll() {
        logger.info("Received request to find all attendee events");
        List<AttendeeEventAssociationDto> adminAttendeeEventDtoList = attendeeEventService.findAll();
        logger.info("Request to retrieve all attendee events completed, found {} attendee events", adminAttendeeEventDtoList.size());
        return adminAttendeeEventDtoList;
    }

    @PutMapping("/{id}")
    public AttendeeEventAssociationDto update(@PathVariable Long id, @RequestBody AttendeeEventAssociationDto attendeeEventAssociationDto) {
        logger.info("Received request to update association between attendee with ID {} and event with ID {}", attendeeEventAssociationDto.getIdAttendee(), attendeeEventAssociationDto.getIdEvent());
        AttendeeEventAssociationDto updated = attendeeEventService.update(id, attendeeEventAssociationDto);
        logger.info("Request to update association between attendee with ID {} and event with ID {} completed", attendeeEventAssociationDto.getIdAttendee(), attendeeEventAssociationDto.getIdEvent());
        return updated;
    }

    @DeleteMapping
    public void delete(@RequestBody AttendeeEventAssociationDto attendeeEventAssociationDto) {
        logger.info("Received request to remove association between attendee with ID {} and event with ID {}", attendeeEventAssociationDto.getIdAttendee(), attendeeEventAssociationDto.getIdEvent());
        attendeeEventService.delete(attendeeEventAssociationDto);
        logger.info("Request to remove association between attendee with ID {} and event with ID {} completed", attendeeEventAssociationDto.getIdAttendee(), attendeeEventAssociationDto.getIdEvent());
    }

}
