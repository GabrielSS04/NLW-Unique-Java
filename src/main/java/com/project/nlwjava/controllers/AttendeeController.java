package com.project.nlwjava.controllers;

import com.project.nlwjava.domain.AttendeeBO;
import com.project.nlwjava.dto.attendee.AttendeeBadgeResponseDTO;
import com.project.nlwjava.services.AttendeeService;
import com.project.nlwjava.services.CheckInService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v2/attendees")
public class AttendeeController {

    private final AttendeeService attendeeService;
    private final CheckInService checkInService;

    @GetMapping("/{id}/badge")
    public ResponseEntity<AttendeeBadgeResponseDTO> getAttendeeBadge(@PathVariable String id, UriComponentsBuilder uriComponentsBuilder){
        AttendeeBadgeResponseDTO response = this.attendeeService.getAttendeeBadge(id, uriComponentsBuilder);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/{id}/checkin")
    public ResponseEntity<Object> registerCheckinAttendee(@PathVariable String id, UriComponentsBuilder uriComponentsBuilder){
        this.attendeeService.checkinAttendee(id);

        var uri = uriComponentsBuilder.path("/attendees/{id}/badge").buildAndExpand(id).toUri();

        return ResponseEntity.status(HttpStatus.CREATED).body(uri);
    }

}
