package com.dev.emsapispring.services.implementations;

import com.dev.emsapispring.entities.dtos.AddEventDto;
import com.dev.emsapispring.entities.dtos.EventDto;
import com.dev.emsapispring.entities.enums.EventType;
import com.dev.emsapispring.entities.enums.MemberEventStatus;
import com.dev.emsapispring.entities.mappers.EventMapper;
import com.dev.emsapispring.entities.models.Attendee;
import com.dev.emsapispring.entities.models.AttendeeEvent;
import com.dev.emsapispring.entities.models.Event;
import com.dev.emsapispring.exceptions.CustomApiException;
import com.dev.emsapispring.exceptions.ExceptionMessage;
import com.dev.emsapispring.repositories.AttendeeRepository;
import com.dev.emsapispring.repositories.EventRepository;
import com.dev.emsapispring.services.interfaces.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventMapper eventMapper;
    private final EventRepository eventRepository;
    private final AttendeeRepository attendeeRepository;
    private static final String ENTITY_NAME = "Event";

    @Override
    public List<EventDto> findAll(EventType type, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        List<Event> eventList;
        
        if (type != null) {
            eventList = eventRepository.findAllByType(type, pageable)
                    .getContent();
        } else {
            eventList = eventRepository.findAll();
        }
        
        return eventList.stream()
                .map(eventMapper::mapToDto)
                .toList();
    }

    @Override
    public EventDto findById(Long id) {
        Optional<Event> eventOptional = eventRepository.findById(id);
        Event event = eventOptional.orElseThrow(() ->
                CustomApiException.builder()
                        .httpStatus(HttpStatus.NOT_FOUND)
                        .message(ExceptionMessage.entityNotFound(ENTITY_NAME))
                        .build());

        return eventMapper.mapToDto(event);
    }

    @Transactional
    @Override
    public EventDto save(AddEventDto addEventDto) {
        eventRepository.findByName(addEventDto.getName()).ifPresent(event -> {
            throw CustomApiException.builder()
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .message(ExceptionMessage.entityAlreadyExists(ENTITY_NAME))
                    .build();
        });

        Optional<Attendee> attendeeOptional = attendeeRepository.findById(addEventDto.getIdAttendee());
        Attendee attendee = attendeeOptional
                .orElseThrow(() -> CustomApiException.builder()
                        .httpStatus(HttpStatus.NOT_FOUND)
                        .message("Attendee not found")
                        .build());

        Event newEvent = eventMapper.mapToEntity(addEventDto);
        Event savedEvent = eventRepository.save(newEvent);

        AttendeeEvent attendeeEvent = AttendeeEvent.builder()
                .attendee(attendee)
                .event(newEvent)
                .status(MemberEventStatus.ACCEPTED)
                .build();

        attendee.getAttendeeEventList().add(attendeeEvent);
        attendeeRepository.save(attendee);

        return eventMapper.mapToDto(savedEvent);
    }

    @Transactional
    @Override
    public EventDto update(Long id, EventDto updatedEventDto) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> CustomApiException.builder()
                        .httpStatus(HttpStatus.NOT_FOUND)
                        .message(ExceptionMessage.entityNotFound(ENTITY_NAME))
                        .build());

        // Check if there is a conflicting event
        boolean isConflictingEventPresent = eventRepository.findAllByName(updatedEventDto.getName()).stream()
                .anyMatch(existingEvent -> !existingEvent.getIdEvent().equals(event.getIdEvent()));

        if (isConflictingEventPresent) {
            throw CustomApiException.builder()
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .message(ExceptionMessage.entityAlreadyExists(ENTITY_NAME))
                    .build();
        }

        event.setName(updatedEventDto.getName());
        event.setType(updatedEventDto.getType());
        event.setStartTimestamp(updatedEventDto.getStartTimestamp());
        event.setEndTimestamp(updatedEventDto.getEndTimestamp());
        event.setLocationName(updatedEventDto.getLocationName());
        event.setDescription(updatedEventDto.getDescription());

        return eventMapper.mapToDto(eventRepository.save(event));
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        if (!eventRepository.existsById(id)) {
            throw CustomApiException.builder()
                    .httpStatus(HttpStatus.NOT_FOUND)
                    .message(ExceptionMessage.entityNotFound(ENTITY_NAME))
                    .build();
        }
        eventRepository.deleteById(id);
    }

}
