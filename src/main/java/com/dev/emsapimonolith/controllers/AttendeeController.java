package com.dev.emsapimonolith.controllers;

import com.dev.emsapimonolith.entities.dtos.EditAttendeeDto;
import com.dev.emsapimonolith.entities.dtos.AttendeeDto;
import com.dev.emsapimonolith.services.interfaces.AttendeeService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/attendees")
@RequiredArgsConstructor
public class AttendeeController {

    private static final Logger logger = LoggerFactory.getLogger(AttendeeController.class);
    private final AttendeeService attendeeService;

    @GetMapping
    public List<AttendeeDto> findAll() {
        logger.info("Received request to find all attendees");
        List<AttendeeDto> attendeeDtoList = attendeeService.findAll();
        logger.info("Request to retrieve all attendees completed, found {} attendees", attendeeDtoList.size());
        return attendeeDtoList;
    }

    @GetMapping("/{id}")
    public AttendeeDto findById(@PathVariable Long id) {
        logger.info("Received request to find attendee by ID: {}", id);
        AttendeeDto attendeeDto = attendeeService.findById(id);
        logger.info("Attendee retrieval completed for ID: {}", id);
        return attendeeDto;
    }

    @PutMapping("/{id}")
    public AttendeeDto update(@PathVariable Long id, @RequestBody EditAttendeeDto updatedAttendeeDto) {
        logger.info("Received request to update attendee with ID: {}", id);
        AttendeeDto updatedAttendee = attendeeService.updateById(id, updatedAttendeeDto);
        logger.info("Attendee updated successfully for ID: {}", updatedAttendee.getIdAttendee());
        return updatedAttendee;
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        logger.info("Received request to delete attendee with ID: {}", id);
        attendeeService.deleteById(id);
        logger.info("Attendee deleted successfully for ID: {}", id);
    }

}
