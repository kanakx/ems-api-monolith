package com.dev.emsapispring.entities.dtos;

import com.dev.emsapispring.entities.enums.EventType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventDto {

    private Long idEvent;
    private String name;
    private EventType type;
    private LocalDateTime startTimestamp;
    private LocalDateTime endTimestamp;
    private String locationName;
    private String description;

}
