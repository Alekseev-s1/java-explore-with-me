package ru.practicum.explore.controller.admin;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.dto.AdminUpdateEventRequest;
import ru.practicum.explore.dto.EventFullDto;
import ru.practicum.explore.model.State;
import ru.practicum.explore.service.events.AdminEventService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/admin/events")
public class AdminEventController {
    private final AdminEventService eventService;

    @Autowired
    public AdminEventController(AdminEventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    public List<EventFullDto> getEvents(@RequestParam List<Long> users,
                                        @RequestParam List<State> states,
                                        @RequestParam List<Long> categories,
                                        @RequestParam String rangeStart,
                                        @RequestParam String rangeEnd,
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
        log.info("Updating event with id {}, updateEvent {}", eventId, adminUpdateEventRequest);
        return eventService.updateEvent(eventId, adminUpdateEventRequest);
    }

    @PatchMapping("/{eventId}/publish")
    public EventFullDto publishEvent(@PathVariable long eventId) {
        log.info("Publishing event with id {}", eventId);
        return eventService.publishEvent(eventId);
    }

    @PatchMapping("/{eventId}/reject")
    public EventFullDto rejectEvent(@PathVariable long eventId) {
        log.info("Rejecting event with id {}", eventId);
        return eventService.rejectEvent(eventId);
    }
}
