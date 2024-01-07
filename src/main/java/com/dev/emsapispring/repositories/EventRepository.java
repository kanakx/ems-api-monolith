package com.dev.emsapispring.repositories;

import com.dev.emsapispring.entities.enums.EventType;
import com.dev.emsapispring.entities.models.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    
    Optional<Event> findByName(String name);
    List<Event> findAllByName(String name);
    Page<Event> findAllByType(EventType type, Pageable pageable);

}
