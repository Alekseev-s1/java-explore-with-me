package ru.practicum.explore.service.events;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explore.dto.AdminUpdateEventRequest;
import ru.practicum.explore.dto.EventFullDto;
import ru.practicum.explore.exception.WrongDateException;
import ru.practicum.explore.exception.WrongStateException;
import ru.practicum.explore.mapper.EventMapper;
import ru.practicum.explore.model.Category;
import ru.practicum.explore.model.Event;
import ru.practicum.explore.model.Location;
import ru.practicum.explore.model.State;
import ru.practicum.explore.repository.CategoryRepository;
import ru.practicum.explore.repository.CustomEventRepository;
import ru.practicum.explore.repository.EventRepository;
import ru.practicum.explore.repository.LocationRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static ru.practicum.explore.exception.UnitNotFoundException.unitNotFoundException;

@Service
@Transactional(readOnly = true)
public class AdminEventService {
    private final CustomEventRepository customEventRepository;
    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final LocationRepository locationRepository;

    @Autowired
    public AdminEventService(CustomEventRepository customEventRepository,
                             EventRepository eventRepository,
                             CategoryRepository categoryRepository,
                             LocationRepository locationRepository) {
        this.customEventRepository = customEventRepository;
        this.eventRepository = eventRepository;
        this.categoryRepository = categoryRepository;
        this.locationRepository = locationRepository;
    }

    public List<EventFullDto> getEvents(List<Long> userIds,
                                        List<State> states,
                                        List<Long> categories,
                                        LocalDateTime rangeStart,
                                        LocalDateTime rangeEnd,
                                        int from,
                                        int size) {
        return customEventRepository.findAllAdminEvents(userIds, states, categories, rangeStart, rangeEnd, from, size)
                .stream()
                .map(EventMapper::toEventFullDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public EventFullDto updateEvent(long eventId, AdminUpdateEventRequest adminUpdateEventRequest) {
        Event eventToUpdate = getEventById(eventId);
        Event newEvent = EventMapper.toEvent(adminUpdateEventRequest, getCategoryById(adminUpdateEventRequest.getCategory()));
        newEvent.setId(eventToUpdate.getId());
        saveLocation(newEvent.getLocation());
        return EventMapper.toEventFullDto(eventRepository.save(newEvent));
    }

    @Transactional
    public EventFullDto publishEvent(long eventId) {
        Event event = getEventById(eventId);

        if (!LocalDateTime.now().isBefore(event.getEventDate().minusHours(1))) {
            throw new WrongDateException("Дата начала события должна быть не ранее чем за час от даты публикации");
        }
        if (!event.getState().equals(State.PENDING)) {
            throw new WrongStateException(
                    String.format("Событие должно быть в состоянии ожидания публикации (%s). " +
                            "Текущее состояние события - %s", State.PENDING, event.getState())
            );
        }

        event.setState(State.PUBLISHED);
        event.setPublishedOn(LocalDateTime.now());
        return EventMapper.toEventFullDto(event);
    }

    @Transactional
    public EventFullDto rejectEvent(long eventId) {
        Event event = getEventById(eventId);

        if (event.getState().equals(State.PUBLISHED)) {
            throw new WrongStateException("Событие не должно быть опубликовано");
        }

        event.setState(State.CANCELED);
        return EventMapper.toEventFullDto(event);
    }

    private Event getEventById(long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(unitNotFoundException("Событие с id = {} не найдено", eventId));
    }

    private Category getCategoryById(long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(unitNotFoundException("Категория с id = {} не найдена", categoryId));
    }

    @Transactional
    public void saveLocation(Location location) {
        Optional<Location> locationalOpt = locationRepository.findLocationByLatAndLon(location.getLat(), location.getLon());
        if (locationalOpt.isEmpty()) {
            locationRepository.save(location);
        }
    }
}
