package com.dev.emsapispring.services.implementations;

import com.dev.emsapispring.entities.dtos.AttendeeDto;
import com.dev.emsapispring.entities.dtos.EditAttendeeDto;
import com.dev.emsapispring.entities.mappers.AttendeeMapper;
import com.dev.emsapispring.entities.models.Attendee;
import com.dev.emsapispring.entities.models.User;
import com.dev.emsapispring.exceptions.CustomApiException;
import com.dev.emsapispring.repositories.AttendeeRepository;
import com.dev.emsapispring.repositories.UserRepository;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AttendeeServiceImplTest {

    @Mock
    AttendeeMapper attendeeMapper;

    @Mock
    AttendeeRepository attendeeRepository;

    @Mock
    UserRepository userRepository;

    @InjectMocks
    AttendeeServiceImpl attendeeService;

    private final EasyRandom easyRandom = new EasyRandom();

    @Test
    void findAll_Success() {
        // Given
        List<Attendee> attendeeList = easyRandom.objects(Attendee.class, 10).toList();
        when(attendeeRepository.findAll()).thenReturn(attendeeList);
        AttendeeDto dummyAttendeeDto = new AttendeeDto();
        when(attendeeMapper.mapToDto(any(Attendee.class))).thenReturn(dummyAttendeeDto);

        // When
        List<AttendeeDto> serviceResult = attendeeService.findAll();

        // Then
        assertNotNull(serviceResult);
        assertEquals(attendeeList.size(), serviceResult.size());
        verify(attendeeRepository, times(1)).findAll();
        verify(attendeeMapper, times(attendeeList.size())).mapToDto(any(Attendee.class));
    }

    @Test
    void findById_Success() {
        // Given
        Long id = easyRandom.nextLong();
        Attendee attendee = easyRandom.nextObject(Attendee.class);

        AttendeeDto attendeeDto = easyRandom.nextObject(AttendeeDto.class);

        when(attendeeRepository.findById(id)).thenReturn(Optional.of(attendee));
        when(attendeeMapper.mapToDto(attendee)).thenReturn(attendeeDto);

        // When
        AttendeeDto serviceResult = attendeeService.findById(id);

        // Then
        assertEquals(attendeeDto, serviceResult);
        verify(attendeeRepository, times(1)).findById(id);
        verify(attendeeMapper, times(1)).mapToDto(attendee);
    }

    @Test
    void findById_Failure_AttendeeNotFound() {
        // Given
        Long id = easyRandom.nextLong();
        when(attendeeRepository.findById(id)).thenReturn(Optional.empty());

        // When
        // Then
        assertThrows(CustomApiException.class, () -> attendeeService.findById(id));
        verify(attendeeRepository, times(1)).findById(id);
        verify(attendeeMapper, never()).mapToDto(any(Attendee.class));
    }

    @Test
    void updateById_Success() {
        // Given
        Long attendeeId = 1L;
        Attendee attendee = easyRandom.nextObject(Attendee.class);
        attendee.setIdAttendee(attendeeId);
        EditAttendeeDto updatedAttendeeDto = easyRandom.nextObject(EditAttendeeDto.class);
        AttendeeDto updatedAttendeeDtoResponse = easyRandom.nextObject(AttendeeDto.class);

        when(attendeeRepository.findById(attendeeId)).thenReturn(Optional.of(attendee));
        when(attendeeRepository.findByUserEmail(updatedAttendeeDto.getEmail())).thenReturn(Optional.empty());
        when(attendeeRepository.save(any(Attendee.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(attendeeMapper.mapToDto(any(Attendee.class))).thenReturn(updatedAttendeeDtoResponse);

        // When
        AttendeeDto serviceResult = attendeeService.updateById(attendeeId, updatedAttendeeDto);

        // Then
        assertEquals(updatedAttendeeDtoResponse, serviceResult);
        verify(attendeeRepository, times(1)).findById(attendeeId);
        verify(attendeeRepository, times(1)).findByUserEmail(updatedAttendeeDto.getEmail());
        verify(attendeeRepository, times(1)).save(any(Attendee.class));
    }

    @Test
    void updateById_Failure_AttendeeNotFound() {
        // Given
        Long id = easyRandom.nextLong();
        EditAttendeeDto updatedAttendeeDto = easyRandom.nextObject(EditAttendeeDto.class);

        when(attendeeRepository.findById(id)).thenReturn(Optional.empty());

        // When
        // Then
        assertThrows(CustomApiException.class, () -> attendeeService.updateById(id, updatedAttendeeDto));
        verify(attendeeRepository, times(1)).findById(id);
        verify(attendeeRepository, never()).findByUserEmail(updatedAttendeeDto.getEmail());
        verify(attendeeRepository, never()).save(any(Attendee.class));
        verify(userRepository, never()).save(any(User.class));
    }


    @Test
    void updateById_Failure_EmailAlreadyExists() {
        // Given
        Long id = easyRandom.nextLong();
        Attendee existingAttendee = easyRandom.nextObject(Attendee.class);
        EditAttendeeDto updatedAttendeeDto = easyRandom.nextObject(EditAttendeeDto.class);
        Attendee anotherAttendeeWithSameEmail = easyRandom.nextObject(Attendee.class);

        when(attendeeRepository.findById(id)).thenReturn(Optional.of(existingAttendee));
        when(attendeeRepository.findByUserEmail(updatedAttendeeDto.getEmail())).thenReturn(Optional.of(anotherAttendeeWithSameEmail));

        // When
        // Then
        assertThrows(CustomApiException.class, () -> attendeeService.updateById(id, updatedAttendeeDto));
        verify(attendeeRepository, times(1)).findById(id);
        verify(attendeeRepository, times(1)).findByUserEmail(updatedAttendeeDto.getEmail());
        verify(attendeeRepository, never()).save(any(Attendee.class));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void deleteById_Success() {
        // Given
        Long id = easyRandom.nextLong();
        when(attendeeRepository.existsById(id)).thenReturn(true);

        // When
        attendeeService.deleteById(id);

        // Then
        verify(attendeeRepository, times(1)).existsById(id);
        verify(attendeeRepository, times(1)).deleteById(id);
    }

    @Test
    void deleteById_Failure_AttendeeNotFound() {
        // Given
        Long id = easyRandom.nextLong();
        when(attendeeRepository.existsById(id)).thenReturn(false);

        // When
        // Then
        assertThrows(CustomApiException.class, () -> attendeeService.deleteById(id));
        verify(attendeeRepository, times(1)).existsById(id);
        verify(attendeeRepository, never()).deleteById(anyLong());
    }

}
