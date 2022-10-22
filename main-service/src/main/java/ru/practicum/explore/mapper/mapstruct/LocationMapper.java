package ru.practicum.explore.mapper.mapstruct;

import org.mapstruct.Mapper;
import ru.practicum.explore.dto.LocationDto;
import ru.practicum.explore.model.Location;

@Mapper(componentModel = "spring")
public interface LocationMapper {
    LocationDto toLocationDto(Location location);

    Location toLocation(LocationDto locationDto);
}
