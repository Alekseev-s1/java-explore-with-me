package ru.practicum.explore.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class CategoryDto {
    @NotNull(message = "id can't be null")
    private Long id;

    @NotBlank(message = "name can't be null or blank")
    private String name;
}
