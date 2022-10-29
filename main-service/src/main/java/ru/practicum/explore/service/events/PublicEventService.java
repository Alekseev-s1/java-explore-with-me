package ru.practicum.explore.service.events;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explore.dto.EndpointHit;
import ru.practicum.explore.dto.EventFullDto;
import ru.practicum.explore.dto.EventShortDto;
import ru.practicum.explore.mapper.EventMapper;
import ru.practicum.explore.model.Event;
import ru.practicum.explore.model.Sort;
import ru.practicum.explore.model.State;
import ru.practicum.explore.repository.CustomEventRepository;
import ru.practicum.explore.repository.EventRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.explore.exception.UnitNotFoundException.unitNotFoundException;

@Service
@Transactional(readOnly = true)
public class PublicEventService {
    private final EventRepository eventRepository;
    private final CustomEventRepository customEventRepository;
    private final EventStatClient eventStatClient;

    @Autowired
    public PublicEventService(EventRepository eventRepository,
                              CustomEventRepository customEventRepository,
                              EventStatClient eventStatClient) {
        this.eventRepository = eventRepository;
        this.customEventRepository = customEventRepository;
        this.eventStatClient = eventStatClient;
    }

    public EventFullDto getEvent(long eventId, EndpointHit endpointHit) {
        eventStatClient.sendHit(endpointHit);

        Event event = eventRepository.findEventByIdAndState(eventId, State.PUBLISHED)
                .orElseThrow(unitNotFoundException("Событие с id = {0} не найдено", eventId));
        event.setViews(event.getViews() + 1);
        return EventMapper.toEventFullDto(event);
    }

    public List<EventShortDto> getEvents(String text,
                                         List<Long> categories,
                                         Boolean paid,
                                         String rangeStart,
                                         String rangeEnd,
                                         boolean onlyAvailable,
                                         Sort sort,
                                         int from,
                                         int size,
                                         EndpointHit endpointHit) {
        eventStatClient.sendHit(endpointHit);

        LocalDateTime start = toLocalDateTime(rangeStart);
        LocalDateTime end = toLocalDateTime(rangeEnd);
        List<Event> events = customEventRepository.findAllPublicEvents(text, categories, paid, start, end, sort, from, size);

        if (onlyAvailable) {
            return events.stream()
                    .filter(event -> event.getParticipantLimit() > event.getConfirmedRequests())
                    .map(EventMapper::toEventShortDto)
                    .collect(Collectors.toList());
        } else {
            return events.stream()
                    .map(EventMapper::toEventShortDto)
                    .collect(Collectors.toList());
        }
    }

    private LocalDateTime toLocalDateTime(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.parse(date, formatter);
    }
}
