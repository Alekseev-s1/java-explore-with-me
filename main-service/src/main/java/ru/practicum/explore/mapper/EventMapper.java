package ru.practicum.explore.mapper;

import org.mapstruct.Mapper;
import ru.practicum.explore.dto.EventFullDto;
import ru.practicum.explore.dto.EventShortDto;
import ru.practicum.explore.model.Event;

@Mapper(componentModel = "spring")
public interface EventMapper {
    EventShortDto toEventShortDto(Event event);

    EventFullDto toEventFullDto(Event event);
}
