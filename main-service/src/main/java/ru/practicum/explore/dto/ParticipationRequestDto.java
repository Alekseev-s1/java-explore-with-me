package ru.practicum.explore.dto;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.explore.model.Status;

import java.time.LocalDateTime;

@Getter
@Setter
public class ParticipationRequestDto {
    private Long id;
    private LocalDateTime created;
    private Long event;
    private Long requester;
    private Status status;
}
