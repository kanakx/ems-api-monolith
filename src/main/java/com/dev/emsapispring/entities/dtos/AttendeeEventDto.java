package com.dev.emsapispring.entities.dtos;

import com.ems.emsdataservicespring.entities.enums.MemberEventStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AttendeeEventDto {

    private Long idAttendeeEvent;
//    private AttendeeDto attendee;
    private EventDto eventDto;
    private MemberEventStatus status;

}
