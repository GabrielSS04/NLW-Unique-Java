package com.project.nlwjava.dto.event;

public record EventDetailsDTO(String id, String title, String details, String slug, Integer maximusAttends, Integer attendeesAmount) {
}
