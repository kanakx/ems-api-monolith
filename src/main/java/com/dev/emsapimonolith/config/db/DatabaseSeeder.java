package com.dev.emsapimonolith.config.db;

import com.dev.emsapimonolith.entities.enums.EventType;
import com.dev.emsapimonolith.entities.enums.AttendeeEventStatus;
import com.dev.emsapimonolith.entities.enums.UserRole;
import com.dev.emsapimonolith.entities.models.Attendee;
import com.dev.emsapimonolith.entities.models.AttendeeEvent;
import com.dev.emsapimonolith.entities.models.Event;
import com.dev.emsapimonolith.entities.models.User;
import com.dev.emsapimonolith.repositories.AttendeeRepository;
import com.dev.emsapimonolith.repositories.EventRepository;
import com.dev.emsapimonolith.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;

@Configuration
public class DatabaseSeeder {

    @Bean
    CommandLineRunner commandLineRunner(
            EventRepository eventRepository,
            AttendeeRepository attendeeRepository,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        return args -> {
            Event event1 = Event.builder()
                    .name("Tech Conference 2023")
                    .type(EventType.CONFERENCE)
                    .startTimestamp(LocalDateTime.parse("2023-03-15T09:00:00"))
                    .endTimestamp(LocalDateTime.parse("2023-03-15T17:00:00"))
                    .locationName("New York")
                    .description("An annual conference for tech enthusiasts and professionals.")
                    .build();

            Event event2 = Event.builder()
                    .name("Art & Design Expo")
                    .type(EventType.EXHIBITION)
                    .startTimestamp(LocalDateTime.parse("2023-04-22T10:00:00"))
                    .endTimestamp(LocalDateTime.parse("2023-04-22T18:00:00"))
                    .locationName("San Francisco")
                    .description("Explore the latest trends in art and design.")
                    .build();

            Event event3 = Event.builder()
                    .name("Live Music Festival")
                    .type(EventType.FESTIVAL)
                    .startTimestamp(LocalDateTime.parse("2023-05-30T12:00:00"))
                    .endTimestamp(LocalDateTime.parse("2023-05-30T23:00:00"))
                    .locationName("Austin")
                    .description("A festival celebrating the best of live music performances.")
                    .build();

            Event event4 = Event.builder()
                    .name("Entrepreneurs Meetup")
                    .type(EventType.MEETUP)
                    .startTimestamp(LocalDateTime.parse("2023-06-10T14:00:00"))
                    .endTimestamp(LocalDateTime.parse("2023-06-10T20:00:00"))
                    .locationName("Chicago")
                    .description("A meetup for aspiring and established entrepreneurs.")
                    .build();

            Event event5 = Event.builder()
                    .name("Health and Wellness Workshop")
                    .type(EventType.WORKSHOP)
                    .startTimestamp(LocalDateTime.parse("2023-07-05T09:00:00"))
                    .endTimestamp(LocalDateTime.parse("2023-07-05T16:00:00"))
                    .locationName("Seattle")
                    .description("A workshop focused on health, wellness, and personal growth.")
                    .build();

            eventRepository.saveAll(List.of(event1, event2, event3, event4, event5));

            User user1 = User.builder()
                    .email("user1@mail.com")
                    .password(passwordEncoder.encode("pwd"))
                    .userRole(UserRole.USER)
                    .build();

            User user2 = User.builder()
                    .email("admin@admin.com")
                    .password(passwordEncoder.encode("pwd"))
                    .userRole(UserRole.ADMIN)
                    .build();

            userRepository.saveAll(List.of(user1, user2));

            Attendee attendee1 = Attendee.builder()
                    .firstName("FirstName 1")
                    .lastName("LastName 1")
                    .user(user1)
                    .build();

            Attendee attendee2 = Attendee.builder()
                    .firstName("FirstName 2")
                    .lastName("LastName 2")
                    .user(user2)
                    .build();

//            attendeeRepository.saveAll(List.of(member1, member2));

            AttendeeEvent attendeeEvent1 = AttendeeEvent.builder()
                    .attendee(attendee1)
                    .event(event1)
                    .status(AttendeeEventStatus.ACCEPTED)
                    .build();
            AttendeeEvent attendeeEvent2 = AttendeeEvent.builder()
                    .attendee(attendee1)
                    .event(event2)
                    .status(AttendeeEventStatus.DECLINED)
                    .build();
            AttendeeEvent attendeeEvent3 = AttendeeEvent.builder()
                    .attendee(attendee2)
                    .event(event3)
                    .status(AttendeeEventStatus.ACCEPTED)
                    .build();

             attendee1.setAttendeeEventList(List.of(attendeeEvent1, attendeeEvent2));
             attendee2.setAttendeeEventList(List.of(attendeeEvent3));
             attendeeRepository.saveAll(List.of(attendee1, attendee2));
        };
    }

}
