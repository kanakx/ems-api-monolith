package com.dev.emsapispring.controllers;

import com.dev.emsapispring.entities.dtos.AddEventDto;
import com.dev.emsapispring.entities.dtos.EventDto;
import com.dev.emsapispring.entities.enums.EventType;
import com.dev.emsapispring.services.interfaces.EventService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventController {

    private static final Logger logger = LoggerFactory.getLogger(EventController.class);
    private final EventService eventService;

    @GetMapping
    public List<EventDto> findAll() {
        logger.info("Received request to find all events");
        List<EventDto> eventDtoList = eventService.findAll();
        logger.info("Events retrieval completed, found {} events", eventDtoList.size());
        return eventDtoList;
    }

    @GetMapping("/{id}")
    public EventDto findById(@PathVariable Long id) {
        logger.info("Received request to find event by ID: {}", id);
        EventDto foundEvent = eventService.findById(id);
        logger.info("Event retrieval completed for ID: {}", id);
        return foundEvent;
    }

    @PostMapping
    public EventDto save(@RequestBody AddEventDto addEventDto) {
        logger.info("Received request to save a new event");
        EventDto savedEvent = eventService.save(addEventDto);
        logger.info("Event saved successfully with ID: {}", savedEvent.getIdEvent());
        return savedEvent;
    }

    @PutMapping("/{id}")
    public EventDto update(@PathVariable Long id, @RequestBody EventDto updatedEventDto) {
        logger.info("Received request to update event with ID: {}", id);
        EventDto updatedEvent = eventService.update(id, updatedEventDto);
        logger.info("Event updated successfully for ID: {}", updatedEvent.getIdEvent());
        return updatedEvent;
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        logger.info("Received request to delete event with ID: {}", id);
        eventService.deleteById(id);
        logger.info("Event deleted successfully for ID: {}", id);
    }

}
