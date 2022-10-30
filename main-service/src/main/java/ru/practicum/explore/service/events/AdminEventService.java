package ru.practicum.explore.service.events;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explore.dto.AdminUpdateEventRequest;
import ru.practicum.explore.dto.EventFullDto;
import ru.practicum.explore.exception.WrongDateException;
import ru.practicum.explore.exception.WrongStateException;
import ru.practicum.explore.mapper.EventMapper;
import ru.practicum.explore.mapper.LocationMapper;
import ru.practicum.explore.model.Category;
import ru.practicum.explore.model.Event;
import ru.practicum.explore.model.Location;
import ru.practicum.explore.model.State;
import ru.practicum.explore.repository.CategoryRepository;
import ru.practicum.explore.repository.CustomEventRepository;
import ru.practicum.explore.repository.EventRepository;
import ru.practicum.explore.repository.LocationRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
                                        String rangeStart,
                                        String rangeEnd,
                                        int from,
                                        int size) {
        LocalDateTime start = toLocalDateTime(rangeStart);
        LocalDateTime end = toLocalDateTime(rangeEnd);

        return customEventRepository.findAllAdminEvents(userIds, states, categories, start, end, from, size)
                .stream()
                .map(EventMapper::toEventFullDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public EventFullDto updateEvent(long eventId, AdminUpdateEventRequest adminUpdateEventRequest) {
        Event eventToUpdate = getEventById(eventId);

        if (adminUpdateEventRequest.getTitle() != null) {
            eventToUpdate.setTitle(adminUpdateEventRequest.getTitle());
        }
        if (adminUpdateEventRequest.getDescription() != null) {
            eventToUpdate.setDescription(adminUpdateEventRequest.getDescription());
        }
        if (adminUpdateEventRequest.getAnnotation() != null) {
            eventToUpdate.setAnnotation(adminUpdateEventRequest.getAnnotation());
        }
        if (adminUpdateEventRequest.getCategory() != null) {
            eventToUpdate.setCategory(getCategoryById(adminUpdateEventRequest.getCategory()));
        }
        if (adminUpdateEventRequest.getLocation() != null) {
            eventToUpdate.setLocation(LocationMapper.toLocation(adminUpdateEventRequest.getLocation()));
        }
        if (adminUpdateEventRequest.getEventDate() != null) {
            eventToUpdate.setEventDate(adminUpdateEventRequest.getEventDate());
        }
        eventToUpdate.setParticipantLimit(adminUpdateEventRequest.getParticipantLimit());
        eventToUpdate.setPaid(adminUpdateEventRequest.isPaid());
        return EventMapper.toEventFullDto(eventToUpdate);
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

    @Transactional
    public void saveLocation(Location location) {
        if (location == null) {
            return;
        }

        Optional<Location> locationalOpt = locationRepository.findLocationByLatAndLon(location.getLat(), location.getLon());
        if (locationalOpt.isEmpty()) {
            locationRepository.save(location);
        }
    }

    private Event getEventById(long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(unitNotFoundException("Событие с id = {0} не найдено", eventId));
    }

    private Category getCategoryById(long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(unitNotFoundException("Категория с id = {0} не найдена", categoryId));
    }

    private LocalDateTime toLocalDateTime(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.parse(date, formatter);
    }
}
