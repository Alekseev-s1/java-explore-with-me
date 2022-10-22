package ru.practicum.explore.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
public class NewUserRequest {
    @NotNull(message = "email can't be null")
    @Email
    private String email;

    @NotBlank(message = "name can't be null or blank")
    private String name;
}
