package ru.practicum.explore.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class NewEventDto {
    @NotBlank(message = "title can't be null or blank")
    @Size(min = 3, max = 120, message = "title should be from 3 to 120 letters")
    private String title;

    @NotBlank(message = "description can't be null or blank")
    @Size(min = 20, max = 7000, message = "description should be from 20 to 7000 letters")
    private String description;

    @NotBlank(message = "annotation can't be null or blank")
    @Size(min = 20, max = 2000, message = "annotation should be from 20 to 2000 letters")
    private String annotation;

    @NotNull(message = "eventDate can't be null")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime eventDate;

    @NotNull(message = "category can't be null")
    private Long category;

    @NotNull(message = "location can't be null")
    private LocationDto location;
    private boolean paid = false;
    private boolean requestModeration = true;
    private long participantLimit;
}
