package com.dev.emsapispring.controllers;

import com.dev.emsapispring.entities.dtos.AttendeeDto;
import com.dev.emsapispring.services.interfaces.AttendeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/attendees")
@RequiredArgsConstructor
public class AttendeeController {

    private final AttendeeService attendeeService;

    @GetMapping("/{id}")
    public AttendeeDto findById(@PathVariable Long id) {
        return attendeeService.findById(id);
    }

}
