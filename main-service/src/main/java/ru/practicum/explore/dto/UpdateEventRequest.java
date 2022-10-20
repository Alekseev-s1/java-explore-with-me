package ru.practicum.explore.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
public class UpdateEventRequest {
    @NotNull(message = "eventId can't be null")
    private Long eventId;

    @Size(min = 3, max = 120, message = "Title should be from 3 to 120 letters")
    private String title;

    @Size(min = 20, max = 7000, message = "Description should be from 20 to 7000 letters")
    private String description;

    @Size(min = 20, max = 2000, message = "Annotation should be from 20 to 2000 letters")
    private String annotation;
    private LocalDateTime eventDate;
    private Long category;
    private Boolean paid;
    private Boolean requestModeration;
    private Integer participantLimit;
}
