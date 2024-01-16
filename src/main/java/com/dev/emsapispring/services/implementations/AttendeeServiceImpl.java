package com.dev.emsapispring.services.implementations;

import com.dev.emsapispring.entities.dtos.AddAttendeeDto;
import com.dev.emsapispring.entities.dtos.AttendeeDto;
import com.dev.emsapispring.entities.dtos.EditAttendeeDto;
import com.dev.emsapispring.entities.mappers.AttendeeMapper;
import com.dev.emsapispring.entities.models.Attendee;
import com.dev.emsapispring.entities.models.User;
import com.dev.emsapispring.exceptions.CustomApiException;
import com.dev.emsapispring.exceptions.ExceptionMessage;
import com.dev.emsapispring.repositories.AttendeeRepository;
import com.dev.emsapispring.repositories.UserRepository;
import com.dev.emsapispring.services.interfaces.AttendeeService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AttendeeServiceImpl implements AttendeeService {

    private static final Logger logger = LoggerFactory.getLogger(AttendeeServiceImpl.class);
    private final AttendeeRepository attendeeRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AttendeeMapper attendeeMapper;
    private static final String ENTITY_NAME = "Attendee";

    @Override
    public List<AttendeeDto> findAll() {
        logger.info("Processing request to find all attendees");
        List<Attendee> attendeeList = attendeeRepository.findAll();
        logger.info("Request to find attendees processed successfully with {} attendees found", attendeeList.size());

        return attendeeList.stream()
                .map(attendeeMapper::mapToDto)
                .toList();
    }

    @Override
    public AttendeeDto findById(Long id) {
        logger.info("Processing request to find attendee by ID: {}", id);
        Optional<Attendee> attendeeOptional = attendeeRepository.findById(id);
        Attendee attendee = attendeeOptional.orElseThrow(() -> {
            logger.warn("Attendee not found for ID: {}", id);

            return CustomApiException.builder()
                    .httpStatus(HttpStatus.NOT_FOUND)
                    .message(ExceptionMessage.entityNotFound(ENTITY_NAME))
                    .build();
        });

        logger.info("Request to find attendee by ID: {} processed successfully", id);

        return attendeeMapper.mapToDto(attendee);
    }

    //TODO test method
    @Transactional
    @Override
    public AttendeeDto update(Long id, EditAttendeeDto updatedAttendeeDto) {
        logger.info("Processing request to update attendee with ID: {}", id);
        Attendee attendee = attendeeRepository.findById(id).orElseThrow(() -> {
            logger.warn("Attempted to update a non-existent attendee with ID {}", id);
            return CustomApiException.builder()
                    .httpStatus(HttpStatus.NOT_FOUND)
                    .message(ExceptionMessage.entityNotFound(ENTITY_NAME))
                    .build();
        });

        attendeeRepository.findByUserEmail(updatedAttendeeDto.getEmail()).ifPresent(existingAttendee -> {
            if (!existingAttendee.getIdAttendee().equals(id)) {
                logger.warn("Attempted to update an event with ID {} with email that already exists: {}", id, updatedAttendeeDto.getEmail());
                throw CustomApiException.builder()
                        .httpStatus(HttpStatus.BAD_REQUEST)
                        .message(ExceptionMessage.entityAlreadyExists(ENTITY_NAME))
                        .build();
            }
        });

        User attendeeUser = attendee.getUser();
        attendeeUser.setEmail(updatedAttendeeDto.getEmail());
        attendeeUser.setUserRole((updatedAttendeeDto.getUserRole()));
        userRepository.save(attendeeUser);

        attendee.setFirstName(updatedAttendeeDto.getFirstName());
        attendee.setLastName(updatedAttendeeDto.getLastName());
        attendee.setUser(attendeeUser);

        Attendee updatedAttendee = attendeeRepository.save(attendee);
        logger.info("Request to update attendee with ID {} processed successfully", updatedAttendee.getIdAttendee());
        return attendeeMapper.mapToDto(updatedAttendee);
    }

    //TODO test method
    @Transactional
    @Override
    public void deleteById(Long id) {
        logger.info("Processing request to delete attendee with ID: {}", id);
        if (!attendeeRepository.existsById(id)) {
            logger.warn("Attempted to delete a non-existent attendee with ID: {}", id);
            throw CustomApiException.builder()
                    .httpStatus(HttpStatus.NOT_FOUND)
                    .message(ExceptionMessage.entityNotFound(ENTITY_NAME))
                    .build();
        }

        attendeeRepository.deleteById(id);
        logger.info("Request to delete attendee with ID {} processed successfully", id);
    }

}
