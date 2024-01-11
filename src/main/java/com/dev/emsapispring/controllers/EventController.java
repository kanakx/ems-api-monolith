package com.dev.emsapispring.controllers;

import com.dev.emsapispring.entities.dtos.AddEventDto;
import com.dev.emsapispring.entities.dtos.EventDto;
import com.dev.emsapispring.entities.enums.EventType;
import com.dev.emsapispring.services.interfaces.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @GetMapping
    public Page<EventDto> findAll(
            @RequestParam(required = false) EventType type,
            @RequestParam(required = false, defaultValue = "0") int pageNo,
            @RequestParam(required = false, defaultValue = "50") int pageSize
    ) {
        return eventService.findAll(type, pageNo, pageSize);
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
