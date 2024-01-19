package com.dev.emsapimonolith.entities.dtos;

import com.dev.emsapimonolith.entities.enums.AttendeeEventStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttendeeEventAssociationDto {

    private Long idAttendeeEvent;
    private Long idAttendee;
    private Long idEvent;
    private AttendeeEventStatus status;

}
