package com.example.eventmanagement.service;

import com.example.eventmanagement.entity.Event;
import com.example.eventmanagement.exception.ApiException;
import com.example.eventmanagement.repository.EventRepository;
import com.example.eventmanagement.repository.RegistrationRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventService {

    private final EventRepository eventRepository;
    private final RegistrationRepository registrationRepository;

    public EventService(EventRepository eventRepository,
                        RegistrationRepository registrationRepository) {
        this.eventRepository = eventRepository;
        this.registrationRepository = registrationRepository;
    }

    public Event create(Event event) {
        if (event.getCapacity() == null || event.getCapacity() <= 0) {
            throw new ApiException("Capacity must be greater than 0", HttpStatus.BAD_REQUEST);
        }

        if (event.getName() == null || event.getName().isBlank()
                || event.getEventDate() == null || event.getEventTime() == null) {
            throw new ApiException("Event name, date and time are required", HttpStatus.BAD_REQUEST);
        }

        event.setStatus("OPEN");
        return eventRepository.save(event);
    }

    public List<Event> getAll() {
        return eventRepository.findAll();
    }

    public Event getById(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new ApiException("Event not found", HttpStatus.NOT_FOUND));
    }

    public Event refreshStatus(Event event) {
        long count = registrationRepository.countByEventId(event.getId());
        if (count >= event.getCapacity()) {
            event.setStatus("FULL");
        } else {
            event.setStatus("OPEN");
        }
        return eventRepository.save(event);
    }
}
