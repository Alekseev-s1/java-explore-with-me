package ru.practicum.explore.controller.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.dto.*;
import ru.practicum.explore.service.CommentService;
import ru.practicum.explore.service.events.UserEventService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("users/{userId}/events")
public class UserEventController {
    private final UserEventService eventService;
    private final CommentService commentService;

    @GetMapping("/{eventId}")
    public EventFullDto getEvent(@PathVariable long userId,
                                 @PathVariable long eventId) {
        log.info("Get event by userId {}, eventId {}", userId, eventId);
        return eventService.getEvent(userId, eventId);
    }

    @GetMapping
    public List<EventShortDto> getEvents(@PathVariable long userId,
                                         @RequestParam(defaultValue = "0") int from,
                                         @RequestParam(defaultValue = "10") int size) {
        log.info("Get events by params: " +
                        "userId {}, " +
                        "from {}, " +
                        "size {}",
                userId, from, size);
        return eventService.getEvents(userId, from, size);
    }

    @PostMapping
    public EventFullDto createEvent(@PathVariable long userId,
                                    @RequestBody @Valid NewEventDto newEventDto) {
        log.info("Creating event by userId {}, event {}", userId, newEventDto);
        return eventService.createEvent(userId, newEventDto);
    }

    @PatchMapping
    public EventFullDto updateEvent(@PathVariable long userId,
                                    @RequestBody @Valid UpdateEventRequest updateEventRequest) {
        log.info("Updating event by userId {}, updateEvent {}", userId, updateEventRequest);
        return eventService.updateEvent(userId, updateEventRequest);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto cancelEvent(@PathVariable long userId,
                                    @PathVariable long eventId) {
        log.info("Canceling event by userId {}, eventId {}", userId, eventId);
        return eventService.cancelEvent(userId, eventId);
    }

    @GetMapping("/{eventId}/requests")
    public List<ParticipationRequestDto> getEventRequests(@PathVariable long userId,
                                                          @PathVariable long eventId) {
        log.info("Get event requests by userId {}, eventId {}", userId, eventId);
        return eventService.getParticipationRequests(userId, eventId);
    }

    @PatchMapping("/{eventId}/requests/{reqId}/confirm")
    public ParticipationRequestDto confirmRequest(@PathVariable long userId,
                                                  @PathVariable long eventId,
                                                  @PathVariable long reqId) {
        log.info("Confirming request by userId {}, eventId {}, reqId {}", userId, eventId, reqId);
        return eventService.confirmRequest(userId, eventId, reqId);
    }

    @PatchMapping("/{eventId}/requests/{reqId}/reject")
    public ParticipationRequestDto rejectRequest(@PathVariable long userId,
                                                  @PathVariable long eventId,
                                                  @PathVariable long reqId) {
        log.info("Rejecting request by userId {}, eventId {}, reqId {}", userId, eventId, reqId);
        return eventService.rejectRequest(userId, eventId, reqId);
    }

    @PostMapping("/{eventId}/comments")
    public CommentResponseDto addComment(@PathVariable long userId,
                                         @PathVariable long eventId,
                                         @RequestBody CommentRequestDto commentRequestDto) {
        log.info("Creating comment userId {}, eventId {}, comment {}", userId, eventId, commentRequestDto);
        return commentService.addComment(userId, eventId, commentRequestDto);
    }

    @PutMapping("{eventId}/comments/{commentId}")
    public CommentResponseDto updateComment(@PathVariable long userId,
                                            @PathVariable long eventId,
                                            @PathVariable long commentId,
                                            @RequestBody CommentRequestDto commentRequestDto) {
        log.info("Updating comment userId {}, eventId {}, commentId {}, comment {}",
                userId,
                eventId,
                commentId,
                commentRequestDto);
        return commentService.updateCommentByUser(userId, eventId, commentId, commentRequestDto);
    }

    @DeleteMapping("/{eventId}/comments/{commentId}")
    public void deleteComment(@PathVariable long userId,
                              @PathVariable long eventId,
                              @PathVariable long commentId) {
        log.info("Deleting comment userId {}, eventId {}, commentId {}", userId, eventId, commentId);
        commentService.deleteCommentByUser(userId, eventId, commentId);
    }
}
