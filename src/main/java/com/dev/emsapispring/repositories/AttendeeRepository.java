package com.dev.emsapispring.repositories;

import com.ems.emsdataservicespring.entities.models.Attendee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AttendeeRepository extends JpaRepository<Attendee, Long> {

    Optional<Attendee> findByIdUser(Long idUser);

}
