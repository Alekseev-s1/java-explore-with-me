package ru.practicum.explore.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class EndpointHit {
    private String app;
    private String uri;
    private String ip;
}
