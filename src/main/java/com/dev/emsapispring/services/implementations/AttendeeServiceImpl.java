package com.dev.emsapispring.services.implementations;

import com.ems.emsdataservicespring.entities.dtos.AttendeeDto;
import com.ems.emsdataservicespring.entities.mappers.AttendeeMapper;
import com.ems.emsdataservicespring.entities.models.Attendee;
import com.ems.emsdataservicespring.exceptions.CustomApiException;
import com.ems.emsdataservicespring.repositories.AttendeeRepository;
import com.ems.emsdataservicespring.services.interfaces.AttendeeService;
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

    @Override
    public AttendeeDto findByIdUser(String idUser) {
        Optional<Attendee> attendeeOptional = attendeeRepository.findByIdUser(Long.valueOf(idUser));
        Attendee attendee = attendeeOptional
                .orElseThrow(() -> CustomApiException.builder()
                        .httpStatus(HttpStatus.NOT_FOUND)
                        .message("Attendee not found")
                        .build());

        return attendeeMapper.mapToDto(attendee);
    }

}
