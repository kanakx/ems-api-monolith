package com.dev.emsapimonolith.entities.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AttendeeDto {

    private Long idAttendee;
    private String firstName;
    private String lastName;
    private List<AttendeeEventDto> attendeeEventDtoList;
    private UserDto userDto;

}
