package com.dev.emsapispring.services.implementations;

import com.ems.emsdataservicespring.entities.dtos.AddEventDto;
import com.ems.emsdataservicespring.entities.dtos.EventDto;
import com.ems.emsdataservicespring.entities.mappers.EventMapper;
import com.ems.emsdataservicespring.entities.models.Event;
import com.ems.emsdataservicespring.exceptions.CustomApiException;
import com.ems.emsdataservicespring.exceptions.ExceptionMessage;
import com.ems.emsdataservicespring.repositories.EventRepository;
import com.ems.emsdataservicespring.services.interfaces.EventService;
import lombok.RequiredArgsConstructor;
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

    @Override
    public List<EventDto> findAll() {
        List<Event> eventList = eventRepository.findAll();
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
                        .message(ExceptionMessage.entityNotFound("Event"))
                        .build());

        return eventMapper.mapToDto(event);
    }

    @Transactional
    @Override
    public EventDto save(AddEventDto addEventDto) {
        eventRepository.findByName(addEventDto.getName()).ifPresent(event -> {
            throw CustomApiException.builder()
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .message(ExceptionMessage.entityAlreadyExists("Event"))
                    .build();
        });

        Event newEvent = eventMapper.mapToEntity(addEventDto);
        return eventMapper.mapToDto(eventRepository.save(newEvent));
    }

    @Transactional
    @Override
    public EventDto update(Long id, EventDto updatedEventDto) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> CustomApiException.builder()
                        .httpStatus(HttpStatus.NOT_FOUND)
                        .message(ExceptionMessage.entityNotFound("Event"))
                        .build());

        // Check if there is a conflicting event
        boolean isConflictingEventPresent = eventRepository.findAllByName(updatedEventDto.getName()).stream()
                .anyMatch(existingEvent -> !existingEvent.getIdEvent().equals(event.getIdEvent()));

        if (isConflictingEventPresent) {
            throw CustomApiException.builder()
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .message(ExceptionMessage.entityAlreadyExists("Event"))
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
                    .message(ExceptionMessage.entityNotFound("Event"))
                    .build();
        }
        eventRepository.deleteById(id);
    }

}
