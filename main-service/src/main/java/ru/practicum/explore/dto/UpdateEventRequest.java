package ru.practicum.explore.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class UpdateEventRequest {
    @NotNull(message = "eventId can't be null")
    private Long eventId;

    @Size(min = 3, max = 120, message = "Title should be from 3 to 120 letters")
    private String title;

    @Size(min = 20, max = 7000, message = "Description should be from 20 to 7000 letters")
    private String description;

    @Size(min = 20, max = 2000, message = "Annotation should be from 20 to 2000 letters")
    private String annotation;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime eventDate;
    private Long category;
    private Boolean paid;
    private Boolean requestModeration;
    private Integer participantLimit;
}
