package ru.practicum.explore.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explore.dto.ParticipationRequestDto;
import ru.practicum.explore.exception.InitiatorIsOwnerException;
import ru.practicum.explore.exception.RequestAlreadyExistsException;
import ru.practicum.explore.exception.RequestsLimitException;
import ru.practicum.explore.exception.WrongStateException;
import ru.practicum.explore.mapper.RequestMapper;
import ru.practicum.explore.model.*;
import ru.practicum.explore.repository.EventRepository;
import ru.practicum.explore.repository.RequestRepository;
import ru.practicum.explore.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.explore.exception.UnitNotFoundException.unitNotFoundException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RequestService {
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    public List<ParticipationRequestDto> getRequests(long userId) {
        return requestRepository.findRequestsByRequesterId(userId).stream()
                .map(RequestMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public ParticipationRequestDto createRequest(long userId, long eventId) {
        Event event = getEventById(eventId);
        User user = getUserById(userId);

        if (requestRepository.findRequestByRequesterAndEvent(user, event).isPresent()) {
            throw new RequestAlreadyExistsException("Нельзя создать повторный запрос. " +
                    "Пользователь уже создавал запрос к этому событию");
        }
        if (event.getInitiator().getId() == userId) {
            throw new InitiatorIsOwnerException("Инициатор события не может добавить запрос на участие в своём событии");
        }
        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new WrongStateException("Нельзя участвовать в неопубликованном событии");
        }
        if (event.getParticipantLimit() == event.getConfirmedRequests()) {
            throw new RequestsLimitException("На данное событие уже достигнут лимит заявок");
        }

        Request request = new Request();
        request.setEvent(event);
        request.setRequester(user);

        if (!event.isRequestModeration() || event.getParticipantLimit() == 0) {
            request.setStatus(RequestStatus.CONFIRMED);
        } else {
            request.setStatus(RequestStatus.PENDING);
        }

        if (event.getParticipantLimit() != 0) {
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
        }

        return RequestMapper.toParticipationRequestDto(requestRepository.save(request));
    }

    @Transactional
    public ParticipationRequestDto cancelRequest(long userId, long reqId) {
        Request request = requestRepository.findRequestByIdAndRequesterId(reqId, userId)
                .orElseThrow(unitNotFoundException("Заявка с id={0}, принадлежащая пользователю с id={1}, не найдена",
                        reqId,
                        userId));
        request.setStatus(RequestStatus.CANCELED);
        return RequestMapper.toParticipationRequestDto(request);
    }

    private User getUserById(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(unitNotFoundException("Пользователь с id={0} не найден", userId));
    }

    public Event getEventById(long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(unitNotFoundException("Событие с id={0} не найдено", eventId));
    }
}
