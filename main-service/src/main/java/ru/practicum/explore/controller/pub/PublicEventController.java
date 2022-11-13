package ru.practicum.explore.controller.pub;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.dto.CommentResponseDto;
import ru.practicum.explore.dto.EndpointHit;
import ru.practicum.explore.dto.EventFullDto;
import ru.practicum.explore.dto.EventShortDto;
import ru.practicum.explore.model.Constant;
import ru.practicum.explore.model.Sort;
import ru.practicum.explore.service.CommentService;
import ru.practicum.explore.service.events.PublicEventService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/events")
public class PublicEventController {
    private final PublicEventService eventService;
    private final CommentService commentService;

    @GetMapping
    public List<EventShortDto> getEvents(@RequestParam(required = false) String text,
                                         @RequestParam(required = false) List<Long> categories,
                                         @RequestParam(required = false) Boolean paid,
                                         @RequestParam(required = false) String rangeStart,
                                         @RequestParam(required = false) String rangeEnd,
                                         @RequestParam(defaultValue = "false") boolean onlyAvailable,
                                         @RequestParam(defaultValue = "EVENT_DATE") Sort sort,
                                         @RequestParam(defaultValue = "0") int from,
                                         @RequestParam(defaultValue = "10") int size,
                                         HttpServletRequest request) {
        log.info("Get events by params: " +
                        "text {}, " +
                        "categories {}, " +
                        "paid {}, " +
                        "rangeStart {}, " +
                        "rangeEnd {}, " +
                        "onlyAvailable {}, " +
                        "sort {}, " +
                        "from, {} " +
                        "size {}",
                text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);
        EndpointHit endpointHit = new EndpointHit(Constant.APP_NAME,
                request.getRequestURI(),
                request.getRemoteAddr(),
                LocalDateTime.now());
        return eventService.getEvents(text,
                categories,
                paid,
                rangeStart,
                rangeEnd,
                onlyAvailable,
                sort,
                from,
                size,
                endpointHit);
    }

    @GetMapping("/{eventId}")
    public EventFullDto getEvent(@PathVariable long eventId, HttpServletRequest request) {
        log.info("Get event by eventId {}", eventId);
        EndpointHit endpointHit = new EndpointHit(Constant.APP_NAME,
                request.getRequestURI(),
                request.getRemoteAddr(),
                LocalDateTime.now());
        return eventService.getEvent(eventId, endpointHit);
    }

    @GetMapping("/{eventId}/comments")
    public List<CommentResponseDto> getComments(@PathVariable long eventId) {
        log.info("Get comments eventId {}", eventId);
        return commentService.getComments(eventId, true);
    }
}
