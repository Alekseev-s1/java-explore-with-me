package ru.practicum.explore.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class EventShortDto {
    private Long id;
    private String title;
    private String annotation;
    private LocalDateTime eventDate;
    private CategoryDto category;
    private UserShortDto initiator;
    private Long confirmedRequests;
    private Boolean paid;
    private Long views;
}
