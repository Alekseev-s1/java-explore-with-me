package ru.practicum.explore.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class EndpointHit {
    private String app;
    private String uri;
    private String ip;
}