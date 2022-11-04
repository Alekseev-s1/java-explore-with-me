package ru.practicum.explore.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.explore.model.CommentState;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class CommentResponseDto {
    private long id;
    private String text;
    private long eventId;
    private UserDto author;
    private LocalDateTime createdAt;
    private CommentState state;
}
