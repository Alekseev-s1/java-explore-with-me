package ru.practicum.explore.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.explore.model.State;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class EventFullDto {
    private Long id;
    private String title;
    private String description;
    private String annotation;
    private CategoryDto category;
    private Long confirmedRequests;
    private LocalDateTime createdOn;
    private LocalDateTime eventDate;
    private LocalDateTime publishedOn;
    private UserShortDto initiator;
    private LocationDto location;
    private Boolean paid;
    private Long participantLimit;
    private Boolean requestModeration;
    private State state;
    private Long views;
}
