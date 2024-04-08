package com.project.nlwjava.repositories;

import com.project.nlwjava.domain.CheckinBO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface CheckinRepository extends JpaRepository<CheckinBO, Integer> {

    Optional<CheckinBO> findByAttendeeId(String attendeeId);

}
