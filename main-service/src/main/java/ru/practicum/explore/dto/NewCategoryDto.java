package ru.practicum.explore.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class NewCategoryDto {
    @NotBlank(message = "name can't be null or blank")
    private String name;
}
