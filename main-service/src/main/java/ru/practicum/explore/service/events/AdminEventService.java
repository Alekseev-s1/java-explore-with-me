package ru.practicum.explore.service.events;

import lombok.RequiredArgsConstructor;
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
import ru.practicum.explore.model.EventState;
import ru.practicum.explore.model.Location;
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
@RequiredArgsConstructor
public class AdminEventService {
    private final CustomEventRepository customEventRepository;
    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final LocationRepository locationRepository;

    public List<EventFullDto> getEvents(List<Long> userIds,
                                        List<EventState> states,
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
            throw new WrongDateException("???????? ???????????? ?????????????? ???????????? ???????? ???? ?????????? ?????? ???? ?????? ???? ???????? ????????????????????");
        }
        if (!event.getState().equals(EventState.PENDING)) {
            throw new WrongStateException(
                    String.format("?????????????? ???????????? ???????? ?? ?????????????????? ???????????????? ???????????????????? (%s). " +
                            "?????????????? ?????????????????? ?????????????? - %s", EventState.PENDING, event.getState())
            );
        }

        event.setState(EventState.PUBLISHED);
        event.setPublishedOn(LocalDateTime.now());
        return EventMapper.toEventFullDto(event);
    }

    @Transactional
    public EventFullDto rejectEvent(long eventId) {
        Event event = getEventById(eventId);

        if (event.getState().equals(EventState.PUBLISHED)) {
            throw new WrongStateException("?????????????? ???? ???????????? ???????? ????????????????????????");
        }

        event.setState(EventState.CANCELED);
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
                .orElseThrow(unitNotFoundException("?????????????? ?? id = {0} ???? ??????????????", eventId));
    }

    private Category getCategoryById(long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(unitNotFoundException("?????????????????? ?? id = {0} ???? ??????????????", categoryId));
    }

    private LocalDateTime toLocalDateTime(String date) {
        if (date == null) {
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.parse(date, formatter);
    }
}
