package com.dev.emsapispring.services.interfaces;

import com.dev.emsapispring.entities.dtos.AddEventDto;
import com.dev.emsapispring.entities.dtos.EventDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface EventService {

    List<EventDto> findAll();
    EventDto findById(Long id);
    EventDto save(AddEventDto addEventDto);
    EventDto update(Long id, EventDto updatedEventDto);
    void deleteById(Long id);

}
