package com.project.nlwjava.repositories;

import com.project.nlwjava.domain.EventBO;
import org.springframework.data.jpa.repository.JpaRepository;


public interface EventRepository extends JpaRepository<EventBO, String> {
}
