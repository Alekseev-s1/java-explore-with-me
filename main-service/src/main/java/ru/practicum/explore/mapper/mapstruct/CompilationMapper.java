package ru.practicum.explore.mapper.mapstruct;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = EventListMapper.class)
public interface CompilationMapper {

}
