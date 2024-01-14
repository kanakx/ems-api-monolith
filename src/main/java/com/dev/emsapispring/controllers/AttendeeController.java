package com.dev.emsapispring.controllers;

import com.dev.emsapispring.entities.dtos.AttendeeDto;
import com.dev.emsapispring.services.interfaces.AttendeeService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/attendees")
@RequiredArgsConstructor
public class AttendeeController {

    private static final Logger logger = LoggerFactory.getLogger(AttendeeController.class);
    private final AttendeeService attendeeService;

    @GetMapping("/{id}")
    public AttendeeDto findById(@PathVariable Long id) {
        logger.info("Received request to find attendee by ID: {}", id);
        AttendeeDto attendeeDto = attendeeService.findById(id);
        logger.info("Attendee retrieval completed for ID: {}", id);
        return attendeeDto;
    }

}
