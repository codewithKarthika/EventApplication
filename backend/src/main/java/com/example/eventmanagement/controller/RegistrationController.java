package com.example.eventmanagement.controller;

import com.example.eventmanagement.entity.Registration;
import com.example.eventmanagement.service.RegistrationService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/registrations")
@CrossOrigin(origins = "http://localhost:5173")
public class RegistrationController {

    private final RegistrationService registrationService;

    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping
    public Registration register(@RequestBody Map<String, Long> request) {
        return registrationService.register(
                request.get("studentId"),
                request.get("eventId")
        );
    }

    @PutMapping("/check-in")
    public Registration checkIn(@RequestBody Map<String, Long> request) {
        return registrationService.checkIn(
                request.get("studentId"),
                request.get("eventId")
        );
    }

    @GetMapping("/summary/{eventId}")
    public Map<String, Object> summary(@PathVariable Long eventId) {
        return registrationService.summary(eventId);
    }
}
