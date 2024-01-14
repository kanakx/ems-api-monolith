package com.dev.emsapispring.services.implementations;

import com.dev.emsapispring.entities.dtos.AddEventDto;
import com.dev.emsapispring.entities.dtos.EventDto;
import com.dev.emsapispring.entities.mappers.EventMapper;
import com.dev.emsapispring.entities.models.Event;
import com.dev.emsapispring.exceptions.CustomApiException;
import com.dev.emsapispring.repositories.EventRepository;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventServiceImplTest {

    @Mock
    EventMapper eventMapper;

    @Mock
    EventRepository eventRepository;

    @InjectMocks
    EventServiceImpl eventService;

    private final EasyRandom easyRandom = new EasyRandom();

    @Test
    void findAll_Success() {
        // Given
        List<Event> eventList = easyRandom.objects(Event.class, 10).toList();
        when(eventRepository.findAll()).thenReturn(eventList);
        when(eventMapper.mapToDto(any(Event.class))).thenAnswer(methodInvocation -> {
            Event event = methodInvocation.getArgument(0);
            return EventDto.builder()
                    .idEvent(event.getIdEvent())
                    .name(event.getName())
                    .type(event.getType())
                    .startTimestamp(event.getStartTimestamp())
                    .endTimestamp(event.getEndTimestamp())
                    .locationName(event.getLocationName())
                    .description(event.getDescription())
                    .build();
        });

        // When
        List<EventDto> serviceResult = eventService.findAll();

        // Then
        assertNotNull(serviceResult);
        assertEquals(eventList.size(), serviceResult.size());
        verify(eventRepository, times(1)).findAll();
        verify(eventMapper, times(eventList.size())).mapToDto(any(Event.class));
    }

    @Test
    void findById_Success() {
        // Given
        Long id = easyRandom.nextLong();
        Event event = easyRandom.nextObject(Event.class);
        EventDto eventDto = easyRandom.nextObject(EventDto.class);
        when(eventRepository.findById(id)).thenReturn(Optional.of(event));
        when(eventMapper.mapToDto(event)).thenReturn(eventDto);

        // When
        EventDto serviceResult = eventService.findById(id);

        // Then
        assertEquals(eventDto, serviceResult);
        verify(eventRepository, times(1)).findById(id);
        verify(eventMapper, times(1)).mapToDto(event);
    }

    @Test
    void findById_Failure_EventNotFound() {
        // Given
        Long id = easyRandom.nextLong();
        when(eventRepository.findById(id)).thenReturn(Optional.empty());

        // When
        // Then
        assertThrows(CustomApiException.class, () -> eventService.findById(id));
        verify(eventRepository, times(1)).findById(id);
        verify(eventMapper, never()).mapToDto(any(Event.class));
    }

    @Test
    void save_Success() {
        // Given
        AddEventDto addEventDto = easyRandom.nextObject(AddEventDto.class);
        Event event = easyRandom.nextObject(Event.class);
        EventDto eventDto = easyRandom.nextObject(EventDto.class);
        when(eventRepository.findByName(addEventDto.getName())).thenReturn(Optional.empty());
        when(eventRepository.save(event)).thenReturn(event);
        when(eventMapper.mapToEntity(addEventDto)).thenReturn(event);
        when(eventMapper.mapToDto(event)).thenReturn(eventDto);

        // When
        EventDto serviceResult = eventService.save(addEventDto);

        // Then
        assertEquals(eventDto, serviceResult);
        verify(eventRepository, times(1)).findByName(addEventDto.getName());
        verify(eventRepository, times(1)).save(event);
        verify(eventMapper, times(1)).mapToEntity(addEventDto);
        verify(eventMapper, times(1)).mapToDto(event);
    }

    @Test
    void save_Failure_EventWithNameAlreadyExists() {
        // Given
        AddEventDto addEventDto = easyRandom.nextObject(AddEventDto.class);
        Event event = easyRandom.nextObject(Event.class);
        when(eventRepository.findByName(addEventDto.getName())).thenReturn(Optional.of(event));

        // When
        // Then
        assertThrows(CustomApiException.class, () -> eventService.save(addEventDto));
        verify(eventRepository, times(1)).findByName(addEventDto.getName());
        verify(eventRepository, never()).save(any(Event.class));
    }

    @Test
    void update_Success() {
        // Given
        Long eventId = 1L;
        Event event = easyRandom.nextObject(Event.class);
        event.setIdEvent(eventId);
        EventDto eventToUpdate = easyRandom.nextObject(EventDto.class);

        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));
        when(eventRepository.findByName(eventToUpdate.getName())).thenReturn(Optional.empty());
        when(eventRepository.save(any(Event.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(eventMapper.mapToDto(any(Event.class))).thenReturn(eventToUpdate);

        // When
        EventDto serviceResult = eventService.update(eventId, eventToUpdate);

        // Then
        assertEquals(eventToUpdate, serviceResult);
        verify(eventRepository, times(1)).findById(eventId);
        verify(eventRepository, times(1)).findByName(eventToUpdate.getName());
        verify(eventRepository, times(1)).save(any(Event.class));
    }

    @Test
    void update_Failure_EventToUpdateNotFound() {
        // Given
        Long id = easyRandom.nextLong();
        EventDto updatedEventDto = easyRandom.nextObject(EventDto.class);
        when(eventRepository.findById(id)).thenReturn(Optional.empty());

        // When
        // Then
        assertThrows(CustomApiException.class, () -> eventService.update(id, updatedEventDto));
        verify(eventRepository, times(1)).findById(id);
        verify(eventRepository, never()).findAllByName(updatedEventDto.getName());
        verify(eventRepository, never()).save(any(Event.class));
    }

    @Test
    void update_Failure_NameConflict() {
        // Given
        Long id = easyRandom.nextLong();
        Event event = easyRandom.nextObject(Event.class);
        EventDto updatedEventDto = easyRandom.nextObject(EventDto.class);
        Event eventWithSameName = easyRandom.nextObject(Event.class);
        when(eventRepository.findById(id)).thenReturn(Optional.of(event));
        when(eventRepository.findByName(updatedEventDto.getName())).thenReturn(Optional.of(eventWithSameName));

        // When
        // Then
        assertThrows(CustomApiException.class, () -> eventService.update(id, updatedEventDto));
        verify(eventRepository, times(1)).findById(id);
        verify(eventRepository, times(1)).findByName(updatedEventDto.getName());
        verify(eventRepository, never()).save(any(Event.class));
    }

    @Test
    void deleteById_Success() {
        // Given
        Long id = easyRandom.nextLong();
        when(eventRepository.existsById(id)).thenReturn(true);

        // When
        eventService.deleteById(id);

        // Then
        verify(eventRepository, times(1)).existsById(id);
        verify(eventRepository, times(1)).deleteById(id);
    }

    @Test
    void deleteById_Failure_EventNotFound() {
        // Given
        Long id = easyRandom.nextLong();
        when(eventRepository.existsById(id)).thenReturn(false);

        // When
        // Then
        assertThrows(CustomApiException.class, () -> eventService.deleteById(id));
        verify(eventRepository, times(1)).existsById(id);
        verify(eventRepository, never()).deleteById(anyLong());
    }

}
