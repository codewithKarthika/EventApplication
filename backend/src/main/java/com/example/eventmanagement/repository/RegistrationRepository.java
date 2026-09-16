package com.example.eventmanagement.repository;

import com.example.eventmanagement.entity.Registration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RegistrationRepository extends JpaRepository<Registration, Long> {
    boolean existsByStudentIdAndEventId(Long studentId, Long eventId);
    long countByEventId(Long eventId);
    Optional<Registration> findByStudentIdAndEventId(Long studentId, Long eventId);
}
