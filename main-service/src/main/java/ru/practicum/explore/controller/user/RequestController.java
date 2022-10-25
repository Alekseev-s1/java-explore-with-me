package ru.practicum.explore.controller.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.dto.ParticipationRequestDto;
import ru.practicum.explore.service.RequestService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users/{userId}/requests")
public class RequestController {
    private final RequestService requestService;

    @Autowired
    public RequestController(RequestService requestService) {
        this.requestService = requestService;
    }

    @GetMapping
    public List<ParticipationRequestDto> getRequests(@PathVariable long userId) {
        log.info("Get requests by userId {}", userId);
        return requestService.getRequests(userId);
    }

    @PostMapping
    public ParticipationRequestDto createRequest(@PathVariable long userId,
                                                 @RequestParam long eventId) {
        log.info("Creating request by userId {}, eventId {}", userId, eventId);
        return requestService.createRequest(userId, eventId);
    }

    @PatchMapping("/{requestId}/cancel")
    private ParticipationRequestDto cancelRequest(@PathVariable long userId,
                                                  @PathVariable long requestId) {
        log.info("Canceling request by requestId {}, userId {}", requestId, userId);
        return requestService.cancelRequest(userId, requestId);
    }
}
