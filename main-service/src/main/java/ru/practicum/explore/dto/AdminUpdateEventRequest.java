package ru.practicum.explore.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminUpdateEventRequest {
    private String title;
    private String description;
    private String annotation;
    private String eventDate;
    private Long category;
    private LocationDto location;
    private Boolean paid = false;
    private Boolean requestModeration = true;
    private Integer participantLimit;
}
