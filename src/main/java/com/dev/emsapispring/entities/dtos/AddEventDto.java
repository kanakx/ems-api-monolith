package com.dev.emsapispring.entities.dtos;

import com.ems.emsdataservicespring.entities.enums.EventType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddEventDto {

    private String name;
    private EventType type;
    private LocalDateTime startTimestamp;
    private LocalDateTime endTimestamp;
    private String locationName;
    private String description;
    private Boolean isPublic;

}
