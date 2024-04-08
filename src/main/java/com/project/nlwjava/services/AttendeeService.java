package com.project.nlwjava.services;

import com.project.nlwjava.domain.AttendeeBO;
import com.project.nlwjava.domain.CheckinBO;
import com.project.nlwjava.dto.attendee.AttendeeBadgeResponseDTO;
import com.project.nlwjava.dto.attendee.AttendeeDetailsDTO;
import com.project.nlwjava.dto.attendee.AttendeeListResponseDTO;
import com.project.nlwjava.dto.attendee.AttendeeBadgeDTO;
import com.project.nlwjava.exceptions.AttendeeAlreadyInEventException;
import com.project.nlwjava.exceptions.AttendeeNotFoundException;
import com.project.nlwjava.repositories.AttendeeRepository;
import com.project.nlwjava.repositories.CheckinRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AttendeeService {

    public final AttendeeRepository attendeeRepo;

    public final CheckInService checkInService;

    public List<AttendeeBO> getAllAttendeeFromEvent(String eventId){
        return this.attendeeRepo.findByEventId(eventId);
    }

    public AttendeeListResponseDTO getEventsAttendee(String eventId){
        List<AttendeeBO> attendeList = this.getAllAttendeeFromEvent(eventId);

        List<AttendeeDetailsDTO> detailsList = attendeList.stream().map(attendeeBO -> {
            Optional<CheckinBO> checkin = this.checkInService.getCheckin(attendeeBO.getId());
            LocalDateTime checkedInAt = checkin.isPresent() ? checkin.get().getCreatedAt() : null;

            return new AttendeeDetailsDTO(attendeeBO.getId(), attendeeBO.getName(), attendeeBO.getEmail(), attendeeBO.getCreatedAt(), checkedInAt);

        }).toList();


        return new AttendeeListResponseDTO(detailsList);
    }

    public AttendeeBO createAttendee(AttendeeBO attendee){
        this.attendeeRepo.save(attendee);
        return attendee;
    }

    public void verifyAttendeeSubscription(String attendeeEmail, String eventId){
        Optional<AttendeeBO> isAttendeeRegister = this.attendeeRepo.findByEventIdAndEmail(attendeeEmail, eventId);
        if (isAttendeeRegister.isPresent()) throw new AttendeeAlreadyInEventException("Attendee is Already In this Event");
    }

    public void checkinAttendee(String attendeeId){
        AttendeeBO attendee = this.getAttendee(attendeeId);
        this.checkInService.registerCheckIn(attendee);
    }

    public AttendeeBO getAttendee(String attendeeId){
        return this.attendeeRepo.findById(attendeeId).orElseThrow(() -> new AttendeeNotFoundException("Attendee not found with ID:" + attendeeId));
    }

    public AttendeeBadgeResponseDTO getAttendeeBadge(String attendeeId, UriComponentsBuilder uriComponentsBuilder){
        AttendeeBO attendee = this.getAttendee(attendeeId);

        var uri = uriComponentsBuilder.path("/attendees/{id}/check-in").buildAndExpand(attendeeId).toUri().toString();

        AttendeeBadgeDTO badgeDTO = new AttendeeBadgeDTO(attendee.getName(), attendee.getEmail(), uri, attendee.getEvent().getId());
        return new AttendeeBadgeResponseDTO(badgeDTO);
    }

}
