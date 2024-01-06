package com.dev.emsapispring.entities.models;

import com.dev.emsapispring.entities.enums.MemberEventStatus;
import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class AttendeeEvent {

    //TODO Adjust entities to use Postgres. Maybe it will work without any changes?
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
    private MemberEventStatus status;

}
