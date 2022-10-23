package ru.practicum.explore.service.events;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explore.dto.*;
import ru.practicum.explore.exception.RequestsLimitException;
import ru.practicum.explore.exception.WrongDateException;
import ru.practicum.explore.exception.WrongStateException;
import ru.practicum.explore.mapper.EventMapper;
import ru.practicum.explore.mapper.RequestMapper;
import ru.practicum.explore.model.*;
import ru.practicum.explore.repository.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static ru.practicum.explore.exception.UnitNotFoundException.unitNotFoundException;

@Service
@Transactional(readOnly = true)
public class UserEventService {
    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final RequestRepository requestRepository;
    private final LocationRepository locationRepository;

    @Autowired
    public UserEventService(EventRepository eventRepository,
                            CategoryRepository categoryRepository,
                            UserRepository userRepository,
                            RequestRepository requestRepository,
                            LocationRepository locationRepository) {
        this.eventRepository = eventRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.requestRepository = requestRepository;
        this.locationRepository = locationRepository;
    }

    public EventFullDto getEvent(long userId, long eventId) {
        return EventMapper.toEventFullDto(getEventByEventAndUserIds(userId, eventId));
    }

    public List<EventShortDto> getEvents(long userId, int from, int size) {
        return eventRepository.findEventsByInitiatorId(userId, PageRequest.of(from / size, size))
                .stream()
                .map(EventMapper::toEventShortDto)
                .collect(Collectors.toList());
    }

    public List<ParticipationRequestDto> getParticipationRequests(long userId, long eventId) {
        Event event = getEventByEventAndUserIds(userId, eventId);
        return requestRepository.findRequestsByEvent(event).stream()
                .map(RequestMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public EventFullDto createEvent(long userId, NewEventDto newEventDto) {
        Event event = EventMapper.toEvent(newEventDto, getCategoryById(newEventDto.getCategory()));

        if (!LocalDateTime.now().isBefore(event.getEventDate().minusHours(2))) {
            throw new WrongDateException("Дата и время, на которые намечено событие, не может быть раньше, чем через два часа от текущего момента");
        }

        User user = getUserById(userId);
        event.setInitiator(user);
        saveLocation(event.getLocation());
        return EventMapper.toEventFullDto(eventRepository.save(event));
    }

    @Transactional
    public EventFullDto updateEvent(long userId, UpdateEventRequest updateEventRequest) {
        Event eventToUpdate = getEventByEventAndUserIds(userId, updateEventRequest.getEventId());

        if (eventToUpdate.getState().equals(State.PUBLISHED)) {
            throw new WrongStateException("Изменить можно только отмененные события или события в состоянии ожидания модерации");
        }
        if (!LocalDateTime.now().isBefore(eventToUpdate.getEventDate().minusHours(2))) {
            throw new WrongDateException("Нельзя изменить событие, до начала которого менее двух часов");
        }
        if (updateEventRequest.getEventDate() != null) {
            if (!LocalDateTime.now().isBefore(updateEventRequest.getEventDate().minusHours(2))) {
                throw new WrongDateException("Дата и время, на которые намечено событие, не может быть раньше, чем через два часа от текущего момента");
            }
        }

        if (eventToUpdate.getState().equals(State.CANCELED)) {
            eventToUpdate.setState(State.PENDING);
        }
        if (updateEventRequest.getTitle() != null) {
            eventToUpdate.setTitle(updateEventRequest.getTitle());
        }
        if (updateEventRequest.getDescription() != null) {
            eventToUpdate.setDescription(updateEventRequest.getDescription());
        }
        if (updateEventRequest.getAnnotation() != null) {
            eventToUpdate.setAnnotation(updateEventRequest.getAnnotation());
        }
        if (updateEventRequest.getEventDate() != null) {
            eventToUpdate.setEventDate(updateEventRequest.getEventDate());
        }
        if (updateEventRequest.getCategory() != null) {
            eventToUpdate.setCategory(getCategoryById(updateEventRequest.getCategory()));
        }
        if (updateEventRequest.getPaid() != null) {
            eventToUpdate.setPaid(updateEventRequest.getPaid());
        }
        if (updateEventRequest.getRequestModeration() != null) {
            eventToUpdate.setRequestModeration(updateEventRequest.getRequestModeration());
        }
        if (updateEventRequest.getParticipantLimit() != null) {
            eventToUpdate.setParticipantLimit(updateEventRequest.getParticipantLimit());
        }

        return EventMapper.toEventFullDto(eventToUpdate);
    }

    @Transactional
    public EventFullDto cancelEvent(long userId, long eventId) {
        Event event = getEventByEventAndUserIds(userId, eventId);

        if (!event.getState().equals(State.PENDING)) {
            throw new WrongStateException("Отменить можно только событие в состоянии ожидания модерации");
        }

        event.setState(State.CANCELED);
        return EventMapper.toEventFullDto(event);
    }

    @Transactional
    public ParticipationRequestDto confirmRequest(long userId, long eventId, long reqId) {
        Event event = getEventByEventAndUserIds(userId, eventId);
        Request request = getRequestById(reqId);

        if (event.getParticipantLimit() == 0 || !event.isRequestModeration()) {
            return RequestMapper.toParticipationRequestDto(request);
        }
        if (event.getParticipantLimit() == event.getConfirmedRequests()) {
            throw new RequestsLimitException("Нельзя подтвердить заявку. Лимит подтвержденных заявок уже исчерпан");
        }
        if (event.getParticipantLimit() == (event.getConfirmedRequests() + 1)) {
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
            request.setStatus(Status.ACCEPTED);
            List<Request> pendingRequests = requestRepository.findRequestsByEventAndStatus(event, Status.PENDING);
            pendingRequests.forEach(req -> req.setStatus(Status.REJECTED));
        }

        return RequestMapper.toParticipationRequestDto(request);
    }

    @Transactional
    public ParticipationRequestDto rejectRequest(long userId, long eventId, long reqId) {
        Event event = getEventByEventAndUserIds(userId, eventId);
        Request request = getRequestById(reqId);
        request.setStatus(Status.REJECTED);
        return RequestMapper.toParticipationRequestDto(request);
    }

    @Transactional
    public void saveLocation(Location location) {
        Optional<Location> locationalOpt = locationRepository.findLocationByLatAndLon(location.getLat(), location.getLon());
        if (locationalOpt.isEmpty()) {
            locationRepository.save(location);
        }
    }

    private Category getCategoryById(long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(unitNotFoundException("Категория с id = {} не найдена", categoryId));
    }

    private User getUserById(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(unitNotFoundException("Пользователь с id = {} не найден", userId));
    }

    private Event getEventByEventAndUserIds(long userId, long eventId) {
        return eventRepository.findEventByIdAndInitiatorId(eventId, userId)
                .orElseThrow(unitNotFoundException("Событие с id = {}, принадлежащее пользователю с id = {} не найдено",
                        eventId,
                        userId));
    }

    private Request getRequestById(long reqId) {
        return requestRepository.findById(reqId)
                .orElseThrow(unitNotFoundException("Заявка с id = {} не найдена", reqId));
    }
}