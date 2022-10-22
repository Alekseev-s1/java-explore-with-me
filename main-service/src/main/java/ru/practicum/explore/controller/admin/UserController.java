package ru.practicum.explore.controller.admin;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.dto.NewUserRequest;
import ru.practicum.explore.dto.UserDto;
import ru.practicum.explore.service.UserService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/admin/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<UserDto> getUsers(@RequestParam List<Long> ids,
                                  @RequestParam(defaultValue = "0") int from,
                                  @RequestParam(defaultValue = "10") int size) {
        log.info("Get users by params:" +
                        "ids {}," +
                        "from {}," +
                        "size {}",
                ids, from, size);
        return userService.getUsers(ids, from, size);
    }

    @PostMapping
    private UserDto createUser(@RequestBody NewUserRequest newUserRequest) {
        log.info("Creating new user {}", newUserRequest);
        return userService.createUser(newUserRequest);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable long userId) {
        log.info("Deleting user by id {}", userId);
        userService.deleteUser(userId);
    }
}
