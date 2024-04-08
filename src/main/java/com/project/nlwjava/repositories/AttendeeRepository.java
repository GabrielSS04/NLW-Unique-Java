package com.project.nlwjava.repositories;

import com.project.nlwjava.domain.AttendeeBO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AttendeeRepository extends JpaRepository<AttendeeBO, String> {

    List<AttendeeBO> findByEventId(String eventId);

    Optional findByEventIdAndEmail(String email, String eventId);

}
