package ru.practicum.explore.controller.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.dto.EventFullDto;
import ru.practicum.explore.dto.EventShortDto;
import ru.practicum.explore.dto.NewEventDto;
import ru.practicum.explore.dto.UpdateEventRequest;
import ru.practicum.explore.service.events.UserEventService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("users/{userId}/events")
public class UserEventController {
    private final UserEventService eventService;

    @Autowired
    public UserEventController(UserEventService eventService) {
        this.eventService = eventService;
    }

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
                                    @RequestBody NewEventDto newEventDto) {
        log.info("Creating event by userId {}, event {}", userId, newEventDto);
        return eventService.createEvent(userId, newEventDto);
    }

    @PatchMapping
    public EventFullDto updateEvent(@PathVariable long userId,
                                    @RequestBody UpdateEventRequest updateEventRequest) {
        log.info("Updating event by userId {}, updateEvent {}", userId, updateEventRequest);
        return eventService.updateEvent(userId, updateEventRequest);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto cancelEvent(@PathVariable long userId,
                                    @PathVariable long eventId) {
        log.info("Canceling event by userId {}, eventId {}", userId, eventId);
        return eventService.cancelEvent(userId, eventId);
    }
}
