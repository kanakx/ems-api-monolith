package com.dev.emsapispring.services.implementations;

import com.dev.emsapispring.entities.dtos.AttendeeEventAssociationDto;
import com.dev.emsapispring.entities.mappers.AttendeeEventMapper;
import com.dev.emsapispring.entities.models.Attendee;
import com.dev.emsapispring.entities.models.AttendeeEvent;
import com.dev.emsapispring.entities.models.Event;
import com.dev.emsapispring.exceptions.CustomApiException;
import com.dev.emsapispring.exceptions.ExceptionMessage;
import com.dev.emsapispring.repositories.AttendeeEventRepository;
import com.dev.emsapispring.repositories.AttendeeRepository;
import com.dev.emsapispring.repositories.EventRepository;
import com.dev.emsapispring.services.interfaces.AttendeeEventService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AttendeeEventServiceImpl implements AttendeeEventService {

    private static final Logger logger = LoggerFactory.getLogger(AttendeeEventServiceImpl.class);
    private final AttendeeRepository attendeeRepository;
    private final AttendeeEventRepository attendeeEventRepository;
    private final EventRepository eventRepository;
    private final AttendeeEventMapper attendeeEventMapper;
    private static final String ENTITY_NAME = "Attendee";

    @Override
    public List<AttendeeEventAssociationDto> findAll() {
        logger.info("Processing request to find all attendees events");
        List<AttendeeEvent> attendeeEventList = attendeeEventRepository.findAll();
        logger.info("Request to find attendees events processed successfully with {} attendees events found", attendeeEventList.size());

        return attendeeEventList.stream()
                .map(attendeeEventMapper::mapToAssociationDto)
                .toList();
    }

    @Override
    public AttendeeEventAssociationDto update(Long id, AttendeeEventAssociationDto attendeeEventAssociationDto) {
        logger.info("Processing request to update AttendeeEvent by ID: {}", id);
        AttendeeEvent attendeeEvent = attendeeEventRepository.findById(id).orElseThrow(() -> {
            logger.warn("Attempted to update a non-existent AttendeeEvent with ID {}", id);
            return CustomApiException.builder()
                    .httpStatus(HttpStatus.NOT_FOUND)
                    .message(ExceptionMessage.entityNotFound(ENTITY_NAME))
                    .build();
        });

        Long idAttendee = attendeeEventAssociationDto.getIdAttendee();
        logger.info("Processing request to find attendee by ID: {}", idAttendee);
        Optional<Attendee> attendeeOptional = attendeeRepository.findById(idAttendee);
        Attendee attendee = attendeeOptional.orElseThrow(() -> {
            logger.warn("Attendee not found for ID: {}", idAttendee);
            return CustomApiException.builder()
                    .httpStatus(HttpStatus.NOT_FOUND)
                    .message(ExceptionMessage.entityNotFound("Attendee"))
                    .build();
        });
        logger.info("Request to find attendee by ID: {} processed successfully", attendee.getIdAttendee());

        Long idEvent = attendeeEventAssociationDto.getIdEvent();
        logger.info("Processing request to find event by ID: {}", idEvent);
        Optional<Event> eventOptional = eventRepository.findById(idEvent);
        Event event = eventOptional.orElseThrow(() -> {
            logger.warn("Attempted to find a non-existing event with ID {}", idEvent);
            return CustomApiException.builder()
                    .httpStatus(HttpStatus.NOT_FOUND)
                    .message(ExceptionMessage.entityNotFound("Event"))
                    .build();
        });
        logger.info("Request to find event by ID: {} processed successfully", event.getIdEvent());


        attendeeEvent.setAttendee(attendee);
        attendeeEvent.setEvent(event);
        attendeeEvent.setStatus(attendeeEventAssociationDto.getStatus());

        AttendeeEvent saved = attendeeEventRepository.save(attendeeEvent);
        logger.info("Request to find event by ID: {} processed successfully", event.getIdEvent());
        return attendeeEventMapper.mapToAssociationDto(saved);
    }

    @Override
    public void delete(AttendeeEventAssociationDto attendeeEventAssociationDto) {
        Optional<AttendeeEvent> attendeeEventOptional = attendeeEventRepository.findByAttendeeIdAttendeeAndEventIdEvent(attendeeEventAssociationDto.getIdAttendee(), attendeeEventAssociationDto.getIdEvent());
        AttendeeEvent attendeeEvent = attendeeEventOptional.orElseThrow(() -> {
            logger.warn("Attempted to find a non-existing attendee event with ID {}", attendeeEventAssociationDto.getIdAttendeeEvent());
            return CustomApiException.builder()
                    .httpStatus(HttpStatus.NOT_FOUND)
                    .message(ExceptionMessage.entityNotFound("AttendeeEvent"))
                    .build();
        });
        logger.info("Request to find attendee event by ID: {} processed successfully", attendeeEvent.getIdAttendeeEvent());
        attendeeEventRepository.delete(attendeeEvent);
    }

}
