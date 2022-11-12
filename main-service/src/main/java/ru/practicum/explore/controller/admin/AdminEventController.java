package ru.practicum.explore.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.dto.AdminUpdateEventRequest;
import ru.practicum.explore.dto.CommentRequestDto;
import ru.practicum.explore.dto.CommentResponseDto;
import ru.practicum.explore.dto.EventFullDto;
import ru.practicum.explore.model.EventState;
import ru.practicum.explore.service.CommentService;
import ru.practicum.explore.service.events.AdminEventService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/events")
public class AdminEventController {
    private final AdminEventService eventService;
    private final CommentService commentService;

    @GetMapping
    public List<EventFullDto> getEvents(@RequestParam(required = false) List<Long> users,
                                        @RequestParam(required = false) List<EventState> states,
                                        @RequestParam(required = false) List<Long> categories,
                                        @RequestParam(required = false) String rangeStart,
                                        @RequestParam(required = false) String rangeEnd,
                                        @RequestParam(defaultValue = "0") int from,
                                        @RequestParam(defaultValue = "10") int size) {
        log.info("Get event by params: " +
                        "users {}, " +
                        "states {}, " +
                        "categories {}, " +
                        "rangeStart {}, " +
                        "rangeEnd {}, " +
                        "from {}, " +
                        "size {}",
                users, states, categories, rangeStart, rangeEnd, from, size);
        return eventService.getEvents(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PutMapping("/{eventId}")
    public EventFullDto updateEvent(@PathVariable long eventId,
                                    @RequestBody AdminUpdateEventRequest adminUpdateEventRequest) {
        log.info("Updating event by admin eventId {}, updateEvent {}", eventId, adminUpdateEventRequest);
        return eventService.updateEvent(eventId, adminUpdateEventRequest);
    }

    @PatchMapping("/{eventId}/publish")
    public EventFullDto publishEvent(@PathVariable long eventId) {
        log.info("Publishing event by admin eventId {}", eventId);
        return eventService.publishEvent(eventId);
    }

    @PatchMapping("/{eventId}/reject")
    public EventFullDto rejectEvent(@PathVariable long eventId) {
        log.info("Rejecting event by admin eventId {}", eventId);
        return eventService.rejectEvent(eventId);
    }

    @GetMapping("/{eventId}/comments")
    public List<CommentResponseDto> getAllComments(@PathVariable long eventId) {
        log.info("Get comments by admin eventId {}", eventId);
        return commentService.getComments(eventId, false);
    }

    @PutMapping("/{eventId}/comments/{commentId}")
    public CommentResponseDto updateComment(@PathVariable long eventId,
                                            @PathVariable long commentId,
                                            @RequestBody @Valid CommentRequestDto commentRequestDto) {
        log.info("Updating comment by admin eventId {}, commentId {}, comment {}", eventId, commentId, commentRequestDto);
        return commentService.updateCommentByAdmin(eventId, commentId, commentRequestDto);
    }

    @DeleteMapping("/comments/{commentId}")
    public void deleteComment(@PathVariable long commentId) {
        log.info("Deleting comment by admin commentId {}", commentId);
        commentService.deleteCommentByAdmin(commentId);
    }
}
