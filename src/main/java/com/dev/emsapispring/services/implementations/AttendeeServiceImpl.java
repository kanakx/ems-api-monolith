package com.dev.emsapispring.services.implementations;

import com.dev.emsapispring.entities.dtos.AttendeeDto;
import com.dev.emsapispring.entities.mappers.AttendeeMapper;
import com.dev.emsapispring.entities.models.Attendee;
import com.dev.emsapispring.exceptions.CustomApiException;
import com.dev.emsapispring.repositories.AttendeeRepository;
import com.dev.emsapispring.services.interfaces.AttendeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AttendeeServiceImpl implements AttendeeService {

    private final AttendeeMapper attendeeMapper;
    private final AttendeeRepository attendeeRepository;

    @Override
    public AttendeeDto findById(Long id) {
        Optional<Attendee> attendeeOptional = attendeeRepository.findById(id);
        Attendee attendee = attendeeOptional
                .orElseThrow(() -> CustomApiException.builder()
                        .httpStatus(HttpStatus.NOT_FOUND)
                        .message("Attendee not found")
                        .build());

        return attendeeMapper.mapToDto(attendee);
    }

}
