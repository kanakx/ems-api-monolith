package com.dev.emsapispring.controllers;

import com.ems.emsdataservicespring.entities.dtos.AttendeeDto;
import com.ems.emsdataservicespring.services.interfaces.AttendeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/data/attendees")
@RequiredArgsConstructor
public class AttendeeController {

    private final AttendeeService attendeeService;

    @GetMapping("/{id}")
    public AttendeeDto findById(@PathVariable Long id) {
        return attendeeService.findById(id);
    }

    @GetMapping
    public AttendeeDto findByUserId(@RequestParam String idUser) {
        return attendeeService.findByIdUser(idUser);
    }

}
