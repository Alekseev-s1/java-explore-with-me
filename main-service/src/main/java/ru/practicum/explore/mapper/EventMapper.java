package ru.practicum.explore.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.explore.dto.AdminUpdateEventRequest;
import ru.practicum.explore.dto.EventFullDto;
import ru.practicum.explore.dto.EventShortDto;
import ru.practicum.explore.dto.NewEventDto;
import ru.practicum.explore.model.Category;
import ru.practicum.explore.model.Event;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EventMapper {
    public static EventShortDto toEventShortDto(Event event) {
        return EventShortDto.builder()
                .id(event.getId())
                .title(event.getTitle())
                .annotation(event.getAnnotation())
                .eventDate(event.getEventDate())
                .category(CategoryMapper.toCategoryDto(event.getCategory()))
                .initiator(UserMapper.toUserShortDto(event.getInitiator()))
                .confirmedRequests(event.getConfirmedRequests())
                .paid(event.isPaid())
                .views(event.getViews())
                .build();
    }

    public static EventFullDto toEventFullDto(Event event) {
        return EventFullDto.builder()
                .id(event.getId())
                .title(event.getTitle())
                .description(event.getDescription())
                .annotation(event.getAnnotation())
                .category(event.getCategory() != null ? CategoryMapper.toCategoryDto(event.getCategory()) : null)
                .confirmedRequests(event.getConfirmedRequests())
                .createdOn(event.getCreatedOn())
                .eventDate(event.getEventDate())
                .publishedOn(event.getPublishedOn())
                .initiator(event.getInitiator() != null ? UserMapper.toUserShortDto(event.getInitiator()) : null)
                .location(event.getLocation() != null ? LocationMapper.toLocationDto(event.getLocation()) : null)
                .paid(event.isPaid())
                .participantLimit(event.getParticipantLimit())
                .requestModeration(event.isRequestModeration())
                .state(event.getState())
                .views(event.getViews())
                .build();
    }

    public static Event toEvent(AdminUpdateEventRequest adminUpdateEventRequest, Category category) {
        Event event = new Event();

        event.setTitle(adminUpdateEventRequest.getTitle());
        event.setAnnotation(adminUpdateEventRequest.getAnnotation());
        event.setDescription(adminUpdateEventRequest.getDescription());
        event.setEventDate(adminUpdateEventRequest.getEventDate());
        event.setCategory(category);
        event.setPaid(adminUpdateEventRequest.isPaid());
        event.setRequestModeration(adminUpdateEventRequest.isRequestModeration());
        event.setParticipantLimit(adminUpdateEventRequest.getParticipantLimit());

        if (adminUpdateEventRequest.getLocation() != null) {
            event.setLocation(LocationMapper.toLocation(adminUpdateEventRequest.getLocation()));
        } else {
            event.setLocation(null);
        }

        return event;
    }

    public static Event toEvent(NewEventDto newEventDto, Category category) {
        Event event = new Event();

        event.setTitle(newEventDto.getTitle());
        event.setAnnotation(newEventDto.getAnnotation());
        event.setDescription(newEventDto.getDescription());
        event.setEventDate(newEventDto.getEventDate());
        event.setCategory(category);
        event.setLocation(LocationMapper.toLocation(newEventDto.getLocation()));
        event.setPaid(newEventDto.isPaid());
        event.setRequestModeration(newEventDto.isRequestModeration());
        event.setParticipantLimit(newEventDto.getParticipantLimit());
        return event;
    }
}
