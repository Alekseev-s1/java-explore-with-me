package ru.practicum.explore.mapper.mapstruct;

import org.mapstruct.Mapper;
import ru.practicum.explore.dto.EventShortDto;
import ru.practicum.explore.model.Event;

import java.util.List;

@Mapper(componentModel = "spring", uses = EventMapper.class)
public interface EventListMapper {
    List<EventShortDto> toEventDtoList(List<Event> events);
}
