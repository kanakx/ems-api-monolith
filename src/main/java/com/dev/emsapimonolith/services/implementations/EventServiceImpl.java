package com.dev.emsapimonolith.services.implementations;

import com.dev.emsapimonolith.entities.dtos.AddEventDto;
import com.dev.emsapimonolith.entities.dtos.EventDto;
import com.dev.emsapimonolith.entities.enums.AttendeeEventStatus;
import com.dev.emsapimonolith.entities.mappers.EventMapper;
import com.dev.emsapimonolith.entities.models.Attendee;
import com.dev.emsapimonolith.entities.models.AttendeeEvent;
import com.dev.emsapimonolith.entities.models.Event;
import com.dev.emsapimonolith.exceptions.CustomApiException;
import com.dev.emsapimonolith.exceptions.ExceptionMessage;
import com.dev.emsapimonolith.repositories.AttendeeEventRepository;
import com.dev.emsapimonolith.repositories.AttendeeRepository;
import com.dev.emsapimonolith.repositories.EventRepository;
import com.dev.emsapimonolith.services.interfaces.EventService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private static final Logger logger = LoggerFactory.getLogger(EventServiceImpl.class);
    private final EventMapper eventMapper;
    private final EventRepository eventRepository;
    private final AttendeeRepository attendeeRepository;
    private final AttendeeEventRepository attendeeEventRepository;
    private static final String ENTITY_NAME = "Event";

    @Override
    public List<EventDto> findAll() {
        logger.info("Processing request to find all events");
        List<Event> eventList = eventRepository.findAll();
        logger.info("Request to find events processed successfully with {} events found", eventList.size());
        return eventList.stream()
                .map(eventMapper::mapToDto)
                .toList();
    }

    @Override
    public EventDto findById(Long id) {
        logger.info("Processing request to find event by ID: {}", id);
        Optional<Event> eventOptional = eventRepository.findById(id);
        Event event = eventOptional.orElseThrow(() -> {
            logger.warn("Attempted to find a non-existing event with ID {}", id);
            return CustomApiException.builder()
                    .httpStatus(HttpStatus.NOT_FOUND)
                    .message(ExceptionMessage.entityNotFound(ENTITY_NAME))
                    .build();
        });

        logger.info("Request to find event by ID: {} processed successfully", id);
        return eventMapper.mapToDto(event);
    }

    @Transactional
    @Override
    public EventDto save(AddEventDto addEventDto) {
        logger.info("Processing request to save a new event with name: {}", addEventDto.getName());


        Long idAttendee = addEventDto.getIdAttendee();
        logger.info("Processing request to find attendee by ID: {}", idAttendee);
        Optional<Attendee> attendeeOptional = attendeeRepository.findById(idAttendee);
        Attendee attendee = attendeeOptional.orElseThrow(() -> {
            logger.warn("Attendee not found for ID: {}", idAttendee);
            return CustomApiException.builder()
                    .httpStatus(HttpStatus.NOT_FOUND)
                    .message(ExceptionMessage.entityNotFound(ENTITY_NAME))
                    .build();
        });

        logger.info("Request to find attendee by ID: {} processed successfully", attendee.getIdAttendee());

        eventRepository.findByName(addEventDto.getName()).ifPresent(event -> {
            logger.warn("Attempted to save an already existing event with name '{}'", addEventDto.getName());
            throw CustomApiException.builder()
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .message(ExceptionMessage.entityAlreadyExists(ENTITY_NAME))
                    .build();
        });

        Event newEvent = Event.builder()
                .name(addEventDto.getName())
                .type(addEventDto.getType())
                .startTimestamp(addEventDto.getStartTimestamp())
                .endTimestamp(addEventDto.getEndTimestamp())
                .locationName(addEventDto.getLocationName())
                .description(addEventDto.getDescription())
                .build();

        Event savedEvent = eventRepository.save(newEvent);

        AttendeeEvent attendeeEvent = AttendeeEvent.builder()
                .attendee(attendee)
                .event(savedEvent)
                .status(AttendeeEventStatus.ACCEPTED)
                .build();

        attendeeEventRepository.save(attendeeEvent);

        logger.info("Request to save event '{}' with ID {} processed successfully", savedEvent.getName(), savedEvent.getIdEvent());
        return eventMapper.mapToDto(savedEvent);
    }

    @Transactional
    @Override
    public EventDto update(Long id, EventDto updatedEventDto) {
        logger.info("Processing request to update an event with ID: {}", id);
        Event eventToUpdate = eventRepository.findById(id).orElseThrow(() -> {
            logger.warn("Attempted to update a non-existent event with ID {}", id);
            return CustomApiException.builder()
                    .httpStatus(HttpStatus.NOT_FOUND)
                    .message(ExceptionMessage.entityNotFound(ENTITY_NAME))
                    .build();
        });

        eventRepository.findByName(updatedEventDto.getName()).ifPresent(existingEvent -> {
            if (!existingEvent.getIdEvent().equals(id)) {
                logger.warn("Attempted to update an event with ID {} with name that already exists: {}", id, updatedEventDto.getName());
                throw CustomApiException.builder()
                        .httpStatus(HttpStatus.BAD_REQUEST)
                        .message(ExceptionMessage.entityAlreadyExists(ENTITY_NAME))
                        .build();
            }
        });

        eventToUpdate.setName(updatedEventDto.getName());
        eventToUpdate.setType(updatedEventDto.getType());
        eventToUpdate.setStartTimestamp(updatedEventDto.getStartTimestamp());
        eventToUpdate.setEndTimestamp(updatedEventDto.getEndTimestamp());
        eventToUpdate.setLocationName(updatedEventDto.getLocationName());
        eventToUpdate.setDescription(updatedEventDto.getDescription());

        Event updatedEvent = eventRepository.save(eventToUpdate);

        logger.info("Request to update event with ID {} processed successfully", updatedEvent.getIdEvent());
        return eventMapper.mapToDto(updatedEvent);
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        logger.info("Processing request to delete event with ID: {}", id);
        if (!eventRepository.existsById(id)) {
            logger.warn("Attempted to delete a non-existent event with ID: {}", id);
            throw CustomApiException.builder()
                    .httpStatus(HttpStatus.NOT_FOUND)
                    .message(ExceptionMessage.entityNotFound(ENTITY_NAME))
                    .build();
        }

        eventRepository.deleteById(id);
        logger.info("Request to delete event with ID {} processed successfully", id);
    }

}
