package ru.practicum.explore.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.explore.dto.EndpointHit;
import ru.practicum.explore.dto.ViewStatsDto;
import ru.practicum.explore.model.Statistic;
import ru.practicum.explore.model.ViewStats;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StatMapper {
    public static Statistic toStatistic(EndpointHit endpointHit) {
        return Statistic.builder()
                .app(endpointHit.getApp())
                .uri(endpointHit.getUri())
                .ip(endpointHit.getIp())
                .timestamp(endpointHit.getTimestamp())
                .build();
    }

    public static ViewStatsDto toViewStats(ViewStats viewStats) {
        return new ViewStatsDto(viewStats.getApp(), viewStats.getUri(), viewStats.getHits());
    }
}
