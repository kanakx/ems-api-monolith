package com.dev.emsapispring.repositories;

import com.dev.emsapispring.entities.models.AttendeeEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AttendeeEventRepository extends JpaRepository<AttendeeEvent, Long> {


    Optional<AttendeeEvent> findByAttendeeIdAttendeeAndEventIdEvent(Long idAttendee, Long idEvent);

}
