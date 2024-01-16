package com.dev.emsapispring.entities.models;

import com.dev.emsapispring.entities.enums.AttendeeEventStatus;
import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "attendees_events")
public class AttendeeEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Long idAttendeeEvent;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Attendee attendee;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Event event;

    @Enumerated(EnumType.STRING)
    @Column(length = 32, nullable = false)
    private AttendeeEventStatus status;

}
