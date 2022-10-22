package ru.practicum.explore.mapper.mapstruct;

import ru.practicum.explore.dto.NewUserRequest;
import ru.practicum.explore.dto.UserDto;
import ru.practicum.explore.dto.UserShortDto;
import ru.practicum.explore.model.User;

public interface UserMapper {
    UserDto toUserDto(User user);

    UserShortDto toUserShortDto(User user);

    User toUser(NewUserRequest newUserRequest);
}
