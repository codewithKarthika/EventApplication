package com.example.eventmanagement.service;

import com.example.eventmanagement.entity.Event;
import com.example.eventmanagement.entity.Registration;
import com.example.eventmanagement.entity.Student;
import com.example.eventmanagement.exception.ApiException;
import com.example.eventmanagement.repository.RegistrationRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.Map;

@Service
public class RegistrationService {

    private final RegistrationRepository registrationRepository;
    private final EventService eventService;
    private final StudentService studentService;

    public RegistrationService(RegistrationRepository registrationRepository,
                                EventService eventService,
                                StudentService studentService) {
        this.registrationRepository = registrationRepository;
        this.eventService = eventService;
        this.studentService = studentService;
    }

    public Registration register(Long studentId, Long eventId) {
        Student student = studentService.getById(studentId);
        Event event = eventService.getById(eventId);

        if (registrationRepository.existsByStudentIdAndEventId(studentId, eventId)) {
            throw new ApiException("Duplicate registration is not allowed", HttpStatus.CONFLICT);
        }

        long registeredCount = registrationRepository.countByEventId(eventId);
        if (registeredCount >= event.getCapacity()) {
            event.setStatus("FULL");
            eventService.create(event);
            throw new ApiException("Event is full. Registration is blocked.", HttpStatus.CONFLICT);
        }

        if (!"OPEN".equals(event.getStatus())) {
            throw new ApiException("Registration is not open", HttpStatus.CONFLICT);
        }

        Registration registration = new Registration();
        registration.setStudent(student);
        registration.setEvent(event);
        registration.setStatus("REGISTERED");

        Registration saved = registrationRepository.save(registration);
        eventService.refreshStatus(event);
        return saved;
    }

    public Registration checkIn(Long studentId, Long eventId) {
        Registration registration = registrationRepository
                .findByStudentIdAndEventId(studentId, eventId)
                .orElseThrow(() -> new ApiException(
                        "Only registered students can check in", HttpStatus.NOT_FOUND));

        Event event = registration.getEvent();

        if (!LocalDate.now().equals(event.getEventDate())) {
            throw new ApiException(
                    "Check-in is allowed only on the event day (" + event.getEventDate() + ")",
                    HttpStatus.CONFLICT);
        }

        if ("CHECKED_IN".equals(registration.getStatus())) {
            throw new ApiException("Student has already checked in", HttpStatus.CONFLICT);
        }

        registration.setStatus("CHECKED_IN");
        registration.setCheckInTime(LocalDateTime.now());

        return registrationRepository.save(registration);
    }

    public Map<String, Object> summary(Long eventId) {
        Event event = eventService.getById(eventId);
        long total = registrationRepository.countByEventId(eventId);

        long checkedIn = registrationRepository.findAll().stream()
                .filter(r -> r.getEvent().getId().equals(eventId))
                .filter(r -> "CHECKED_IN".equals(r.getStatus()))
                .count();

        return Map.of(
                "eventId", event.getId(),
                "eventName", event.getName(),
                "capacity", event.getCapacity(),
                "registeredCount", total,
                "checkedInCount", checkedIn,
                "status", event.getStatus()
        );
    }
}
