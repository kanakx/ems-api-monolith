package com.dev.emsapispring.entities.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Attendee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Long idAttendee;

    @Column(length = 100, nullable = false)
    private String fullName;

    @Column(nullable = false)
    private Long idUser;

    @OneToMany(mappedBy = "attendee", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<AttendeeEvent> attendeeEventList = new ArrayList<>();

}
