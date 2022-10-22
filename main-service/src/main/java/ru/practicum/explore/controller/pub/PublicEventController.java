package ru.practicum.explore.controller.pub;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.dto.EventFullDto;
import ru.practicum.explore.dto.EventShortDto;
import ru.practicum.explore.model.Sort;
import ru.practicum.explore.model.State;
import ru.practicum.explore.service.EventService;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/events")
public class PublicEventController {
    private final EventService eventService;

    @Autowired
    public PublicEventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    public List<EventShortDto> getEvents(@RequestParam(required = false) String text,
                                         @RequestParam(required = false) List<Long> categories,
                                         @RequestParam(required = false) Boolean paid,
                                         @RequestParam(required = false) LocalDateTime rangeStart,
                                         @RequestParam(required = false) LocalDateTime rangeEnd,
                                         @RequestParam(defaultValue = "false") boolean onlyAvailable,
                                         @RequestParam Sort sort,
                                         @RequestParam(defaultValue = "0") int from,
                                         @RequestParam(defaultValue = "10") int size) {
        log.info("Get events by params: text {}," +
                "categories {}," +
                "paid {}," +
                "rangeStart {}," +
                "rangeEnd {}," +
                "onlyAvailable {}," +
                "sort {}," +
                "from, {}" +
                "size {}",
                text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);
        return eventService.getEvents(text,
                categories,
                paid,
                rangeStart,
                rangeEnd,
                onlyAvailable,
                sort,
                State.PUBLISHED,
                from,
                size);
    }

    @GetMapping("/{eventId}")
    public EventFullDto getEvent(@PathVariable long eventId) {
        log.info("Get event by eventId {}", eventId);
        return eventService.getEvent(eventId);
    }
}
