package com.project.nlwjava.dto.event;


import com.project.nlwjava.domain.EventBO;
import com.project.nlwjava.dto.event.EventDetailsDTO;
import lombok.Getter;

@Getter
public class EventResponseDTO {

    EventDetailsDTO event;

    public EventResponseDTO(EventBO bo, Integer numberOfAttendeers){
        this.event = new EventDetailsDTO(bo.getId(), bo.getTitle(), bo.getDetails(), bo.getSlug(), bo.getMaximusAttends(), numberOfAttendeers);

    }
}
