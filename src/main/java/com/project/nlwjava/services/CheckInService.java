package com.project.nlwjava.services;

import com.project.nlwjava.domain.AttendeeBO;
import com.project.nlwjava.domain.CheckinBO;
import com.project.nlwjava.exceptions.CheckinAlredyRegisterException;
import com.project.nlwjava.repositories.CheckinRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CheckInService {
    private final CheckinRepository checkinRepository;

    public void registerCheckIn(AttendeeBO attendee){

        this.verifyCheckInExist(attendee.getId());

        CheckinBO checkin = new CheckinBO();
        checkin.setAttendee(attendee);
        checkin.setCreatedAt(LocalDateTime.now());
        this.checkinRepository.save(checkin);
    }

    public Optional<CheckinBO> getCheckin(String attendeeId){
        return this.checkinRepository.findByAttendeeId(attendeeId);
    }

    private void verifyCheckInExist(String attendeeId){
        Optional<CheckinBO> isCheckin = this.getCheckin(attendeeId);
        if (isCheckin.isPresent()) throw new CheckinAlredyRegisterException("Checkin is already register");
    }

}
