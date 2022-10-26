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
        Statistic statistic = new Statistic();

        statistic.setApp(endpointHit.getApp());
        statistic.setUri(endpointHit.getUri());
        statistic.setIp(endpointHit.getIp());
        return statistic;
    }

    public static ViewStatsDto toViewStats(ViewStats viewStats) {
        return new ViewStatsDto(viewStats.getApp(), viewStats.getUri(), viewStats.getHits());
    }
}
