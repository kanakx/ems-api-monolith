package com.dev.emsapispring.repositories;

import com.ems.emsdataservicespring.entities.models.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    
    Optional<Event> findByName(String name);
    List<Event> findAllByName(String name);

}
