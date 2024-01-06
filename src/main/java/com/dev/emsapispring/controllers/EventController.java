package com.dev.emsapispring.controllers;

import com.ems.emsdataservicespring.entities.dtos.AddEventDto;
import com.ems.emsdataservicespring.entities.dtos.EventDto;
import com.ems.emsdataservicespring.services.interfaces.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/data/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @GetMapping
    public List<EventDto> findAll() {
        return eventService.findAll();
    }

    @GetMapping("/{id}")
    public EventDto findById(@PathVariable Long id) {
        return eventService.findById(id);
    }

    @PostMapping
    public EventDto save(@RequestBody AddEventDto addEventDto) {
        return eventService.save(addEventDto);
    }

    @PutMapping("/{id}")
    public EventDto update(@PathVariable Long id, @RequestBody EventDto updatedEventDto) {
        return eventService.update(id, updatedEventDto);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        eventService.deleteById(id);
    }

}
