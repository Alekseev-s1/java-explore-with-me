package ru.practicum.explore.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.explore.dto.EndpointHit;
import ru.practicum.explore.dto.ViewStatsDto;
import ru.practicum.explore.mapper.StatMapper;
import ru.practicum.explore.model.Statistic;
import ru.practicum.explore.repository.StatRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StatService {
    private final StatRepository statRepository;

    @Autowired
    public StatService(StatRepository statRepository) {
        this.statRepository = statRepository;
    }

    public void createHit(EndpointHit endpointHit) {
        Statistic statistic = StatMapper.toStatistic(endpointHit);
        statRepository.save(statistic);
    }

    public List<ViewStatsDto> getStatistic(String start, String end, List<String> uris, boolean unique) {
        LocalDateTime startDate = toLocalDateTime(start);
        LocalDateTime endDate = toLocalDateTime(end);

        if (unique) {
            return statRepository.getUniqueViews(startDate, endDate, uris).stream()
                    .map(StatMapper::toViewStats)
                    .collect(Collectors.toList());
        } else {
            return statRepository.getViews(startDate, endDate, uris).stream()
                    .map(StatMapper::toViewStats)
                    .collect(Collectors.toList());
        }
    }

    private LocalDateTime toLocalDateTime(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.parse(date, formatter);
    }
}
