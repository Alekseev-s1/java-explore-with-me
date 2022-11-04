package ru.practicum.explore.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class EndpointHit {
    private String app;
    private String uri;
    private String ip;
    private LocalDateTime timestamp;
}
