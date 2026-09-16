package com.example.eventmanagement.controller;

import com.example.eventmanagement.entity.Event;
import com.example.eventmanagement.service.EventService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
@CrossOrigin(origins = "http://localhost:5173")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping
    public Event create(@RequestBody Event event) {
        return eventService.create(event);
    }

    @GetMapping
    public List<Event> getAll() {
        return eventService.getAll();
    }
}
