package com.example.eventmanagement.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(
    name = "registrations",
    uniqueConstraints = @UniqueConstraint(columnNames = {"student_id", "event_id"})
)
public class Registration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne(optional = false)
    @JoinColumn(name = "event_id")
    private Event event;

    @Column(nullable = false)
    private String status = "REGISTERED";

    private LocalDateTime checkInTime;

    public Registration() {}

    public Long getId() { return id; }
    public Student getStudent() { return student; }
    public Event getEvent() { return event; }
    public String getStatus() { return status; }
    public LocalDateTime getCheckInTime() { return checkInTime; }

    public void setId(Long id) { this.id = id; }
    public void setStudent(Student student) { this.student = student; }
    public void setEvent(Event event) { this.event = event; }
    public void setStatus(String status) { this.status = status; }
    public void setCheckInTime(LocalDateTime checkInTime) { this.checkInTime = checkInTime; }
}
