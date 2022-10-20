package ru.practicum.explore.dto;

import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Setter
public class ApiError {
    private List<String> errors;
    private String message;
    private String reason;
    private String status;
    private LocalDateTime timestamp;
}
