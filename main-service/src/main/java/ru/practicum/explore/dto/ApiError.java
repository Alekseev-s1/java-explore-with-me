package ru.practicum.explore.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
public class ApiError {
    private String message;
    private String reason;
    private String status;
    private LocalDateTime timestamp;
    private List<String> errors;
}
