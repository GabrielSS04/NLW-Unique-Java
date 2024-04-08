package com.project.nlwjava.services;

import com.project.nlwjava.domain.AttendeeBO;
import com.project.nlwjava.domain.EventBO;
import com.project.nlwjava.dto.attendee.AttendeeIdDTO;
import com.project.nlwjava.dto.attendee.AttendeeRequestDTO;
import com.project.nlwjava.dto.event.EventIdDTO;
import com.project.nlwjava.dto.event.EventRequestDTO;
import com.project.nlwjava.dto.event.EventResponseDTO;
import com.project.nlwjava.exceptions.EventFullException;
import com.project.nlwjava.exceptions.EventNotFoundException;
import com.project.nlwjava.repositories.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.time.LocalDateTime;
import java.util.List;


@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepo;
    private final AttendeeService attendeeService;

    public EventResponseDTO getEventDetails(String eventId){
        EventBO event = this.getEventById(eventId);
        List<AttendeeBO> attendeeList = this.attendeeService.getAllAttendeeFromEvent(eventId);
        return new EventResponseDTO(event, attendeeList.size());
    }

    public EventIdDTO createEvent(EventRequestDTO dto){
        EventBO bo = new EventBO();
        bo.setTitle(dto.title());
        bo.setDetails(dto.details());
        bo.setMaximusAttends(dto.maximumAttends());
        bo.setSlug(createSlug(dto.title()));

        this.eventRepo.save(bo);

        return new EventIdDTO(bo.getId());
    }

    public AttendeeIdDTO registerAttendeeOnEvent(AttendeeRequestDTO attendee, String eventId){
        this.attendeeService.verifyAttendeeSubscription(attendee.email(), eventId);

        EventBO event = this.getEventById(eventId);
        List<AttendeeBO> attendeeList = this.attendeeService.getAllAttendeeFromEvent(eventId);

        if (event.getMaximusAttends() <= attendeeList.size()) throw new EventFullException("This Event is Full");

        AttendeeBO bo = new AttendeeBO();
        bo.setName(attendee.name());
        bo.setEmail(attendee.email());
        bo.setEvent(event);
        bo.setCreatedAt(LocalDateTime.now());

        this.attendeeService.createAttendee(bo);

        return new AttendeeIdDTO(bo.getId());

    }


    private EventBO getEventById(String eventId){
        EventBO event = this.eventRepo.findById(eventId).orElseThrow(() -> new EventNotFoundException("Event not found with ID: " + eventId));

        return event;
    }

    private String createSlug(String text){
        String normalize = Normalizer.normalize(text, Normalizer.Form.NFD);

        return normalize.replaceAll("[\\p{InCOMBINING_DIACRITICAL_MARKS}]", "")
                .replaceAll("[^\\w\\s]","")
                .replaceAll("[\\s+]","-")
                .toLowerCase();
    }

}
