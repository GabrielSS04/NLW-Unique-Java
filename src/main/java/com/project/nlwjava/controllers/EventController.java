package com.project.nlwjava.controllers;

import com.project.nlwjava.dto.attendee.AttendeeIdDTO;
import com.project.nlwjava.dto.attendee.AttendeeListResponseDTO;
import com.project.nlwjava.dto.attendee.AttendeeRequestDTO;
import com.project.nlwjava.dto.event.EventIdDTO;
import com.project.nlwjava.dto.event.EventRequestDTO;
import com.project.nlwjava.dto.event.EventResponseDTO;
import com.project.nlwjava.services.AttendeeService;
import com.project.nlwjava.services.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/events")
public class EventController {

    private final EventService eventService;

    private final AttendeeService attendeeService;

    @GetMapping("/{id}")
    public ResponseEntity getEvent(@PathVariable String id){
        EventResponseDTO response = this.eventService.getEventDetails(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/attendees/{id}")
    public ResponseEntity<AttendeeListResponseDTO> getEventsAttendee(@PathVariable String id){
        AttendeeListResponseDTO response = this.attendeeService.getEventsAttendee(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/create")
    public ResponseEntity<Object> createEvent(@RequestBody EventRequestDTO dto, UriComponentsBuilder uriComponentsBuilder){
        EventIdDTO id = this.eventService.createEvent(dto);
        var uri = uriComponentsBuilder.path("/v1/event/{id}").buildAndExpand(id.eventId()).toUri();
        return ResponseEntity.created(uri).body("Event created with ID:" + id.eventId());
    }

    @PostMapping("/{id}/attendees")
    public ResponseEntity<AttendeeIdDTO> registerPartcipant(@PathVariable String id, @RequestBody AttendeeRequestDTO dto, UriComponentsBuilder uriComponentsBuilder){
        AttendeeIdDTO response = this.eventService.registerAttendeeOnEvent(dto, id);
        var uri = uriComponentsBuilder.path("/v2/attendees/{id}/badge").buildAndExpand(response.id()).toUri();
        return ResponseEntity.created(uri).body(response);
    }


}
