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
public class AttendeeEventDto {

    private Long idAttendeeEvent;
    private EventDto eventDto;
    private AttendeeEventStatus status;

}
