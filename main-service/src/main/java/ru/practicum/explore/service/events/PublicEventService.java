package ru.practicum.explore.service.events;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explore.dto.EventFullDto;
import ru.practicum.explore.dto.EventShortDto;
import ru.practicum.explore.mapper.EventMapper;
import ru.practicum.explore.model.Event;
import ru.practicum.explore.model.Sort;
import ru.practicum.explore.model.State;
import ru.practicum.explore.repository.CustomEventRepository;
import ru.practicum.explore.repository.EventRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.explore.exception.UnitNotFoundException.unitNotFoundException;

@Service
@Transactional(readOnly = true)
public class PublicEventService {
    private final EventRepository eventRepository;
    private final CustomEventRepository customEventRepository;

    @Autowired
    public PublicEventService(EventRepository eventRepository, CustomEventRepository customEventRepository) {
        this.eventRepository = eventRepository;
        this.customEventRepository = customEventRepository;
    }

    public EventFullDto getEvent(long eventId) {
        return EventMapper.toEventFullDto(
                eventRepository.findEventByIdAndState(eventId, State.PUBLISHED)
                        .orElseThrow(unitNotFoundException("Событие с id = {} не найдено", eventId))
        );
    }

    public List<EventShortDto> getEvents(String text,
                                         List<Long> categories,
                                         Boolean paid,
                                         LocalDateTime rangeStart,
                                         LocalDateTime rangeEnd,
                                         boolean onlyAvailable,
                                         Sort sort,
                                         int from,
                                         int size) {
        List<Event> events = customEventRepository.findAllPublicEvents(text, categories, paid, rangeStart, rangeEnd, sort, from, size);

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


}
