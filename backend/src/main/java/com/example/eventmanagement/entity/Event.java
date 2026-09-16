package com.example.eventmanagement.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private LocalDate eventDate;

    @Column(nullable = false)
    private LocalTime eventTime;

    @Column(nullable = false)
    private Integer capacity;

    @Column(nullable = false)
    private String status = "OPEN";

    public Event() {}

    public Long getId() { return id; }
    public String getName() { return name; }
    public LocalDate getEventDate() { return eventDate; }
    public LocalTime getEventTime() { return eventTime; }
    public Integer getCapacity() { return capacity; }
    public String getStatus() { return status; }

    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setEventDate(LocalDate eventDate) { this.eventDate = eventDate; }
    public void setEventTime(LocalTime eventTime) { this.eventTime = eventTime; }
    public void setCapacity(Integer capacity) { this.capacity = capacity; }
    public void setStatus(String status) { this.status = status; }
}
